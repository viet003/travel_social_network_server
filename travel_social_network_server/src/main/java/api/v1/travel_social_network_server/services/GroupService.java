package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dtos.group.UpdateGroupDto;
import api.v1.travel_social_network_server.entities.*;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.PageableResponse;
import api.v1.travel_social_network_server.reponses.group.GroupResponse;
import api.v1.travel_social_network_server.responsitories.GroupRepository;
import api.v1.travel_social_network_server.responsitories.GroupMemberRepository;
import api.v1.travel_social_network_server.utilities.GroupRoleEnum;
import api.v1.travel_social_network_server.utilities.PrivacyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private static final String GROUP_FOLDER = "groups";

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public PageableResponse<GroupResponse> getAllGroupsByName(User user, String keyword, String privacy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Group> groups;

        // Xử lý privacy
        boolean filterPrivacy = !"all".equalsIgnoreCase(privacy);
        PrivacyEnum privacyEnum = null;

        if (filterPrivacy) {
            try {
                privacyEnum = PrivacyEnum.valueOf(privacy.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Privacy không hợp lệ. Giá trị hợp lệ: all, public, private");
            }
        }

        // Lọc theo keyword + privacy
        if (keyword == null || keyword.trim().isEmpty()) {
            if (filterPrivacy) {
                groups = groupRepository.findAllByPrivacy(privacyEnum, pageable);
            } else {
                groups = groupRepository.findAll(pageable);
            }
        } else {
            if (filterPrivacy) {
                groups = groupRepository.findAllByGroupNameContainingAndPrivacy(keyword.trim(), privacyEnum, pageable);
            } else {
                groups = groupRepository.findAllByGroupNameContaining(keyword.trim(), pageable);
            }
        }

        System.out.println("Found groups for keyword '{}': {}" + keyword + groups.getContent());

        List<GroupResponse> content = groups.getContent().stream()
                .map(group -> convertToGroupResponse(group, user))
                .toList();

        return PageableResponse.<GroupResponse>builder()
                .content(content)
                .totalElements(groups.getTotalElements())
                .totalPages(groups.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public GroupResponse getGroupById(UUID groupId, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .coverImageUrl(group.getCoverImageUrl())
                .memberCount(group.getMemberCount() != null ? group.getMemberCount() : 0)
                .privacy(group.getPrivacy().equals(PrivacyEnum.PRIVATE))
                .isMember(groupMemberRepository.existsByGroupGroupIdAndUserUserId(groupId, user.getUserId()))
                .build();
    }

    @Transactional
    public GroupResponse createGroup(User user, UpdateGroupDto updateGroupDto) throws IOException {
        if (updateGroupDto.getName() == null || updateGroupDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }

        Group group = Group.builder()
                .groupName(updateGroupDto.getName().trim())
                .groupDescription(updateGroupDto.getDescription() != null ? updateGroupDto.getDescription().trim() : null)
                .privacy(updateGroupDto.getPrivacy() ? PrivacyEnum.PRIVATE : PrivacyEnum.PUBLIC)
                .groupMembers(new ArrayList<>())
                .memberCount(1)
                .build();

        if (updateGroupDto.getCover() != null && !updateGroupDto.getCover().isEmpty()) {
            try {
                String coverUrl = cloudinaryService.uploadFile(updateGroupDto.getCover().getBytes(), GROUP_FOLDER, "image");
                group.setCoverImageUrl(coverUrl);
            } catch (IOException e) {
                log.warn("Failed to upload cover for group '{}': {}", updateGroupDto.getName(), e.getMessage());
            }
        }

        group = groupRepository.save(group);

        GroupMember creatorMember = GroupMember.builder()
                .group(group)
                .user(user)
                .role(GroupRoleEnum.OWNER)
                .build();

        groupMemberRepository.save(creatorMember);
        group.getGroupMembers().add(creatorMember);
        group = groupRepository.save(group);

        log.info("Created group '{}' with ID: {}", group.getGroupName(), group.getGroupId());
        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .coverImageUrl(group.getCoverImageUrl())
                .memberCount(group.getMemberCount() != null ? group.getMemberCount() : 0)
                .groupDescription(group.getGroupDescription())
                .privacy(group.getPrivacy().equals(PrivacyEnum.PRIVATE))
                .isMember(true)
                .build();
    }


    private GroupResponse convertToGroupResponse(Group group, User user) {
        boolean isMember = groupMemberRepository.existsByGroupGroupIdAndUserUserId(group.getGroupId(), user.getUserId());

        return GroupResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .coverImageUrl(group.getCoverImageUrl())
                .memberCount(group.getMemberCount() != null ? group.getMemberCount() : 0)
                .privacy(group.getPrivacy().equals(PrivacyEnum.PRIVATE))
                .isMember(isMember)
                .build();
    }
}
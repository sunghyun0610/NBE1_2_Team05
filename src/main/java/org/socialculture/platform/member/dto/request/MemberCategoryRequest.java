package org.socialculture.platform.member.dto.request;


import java.util.List;

public record MemberCategoryRequest(
        List<Long> categories
) {}

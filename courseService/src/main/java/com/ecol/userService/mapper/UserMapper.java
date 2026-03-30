package com.ecol.userService.mapper;
import com.ecol.userService.dto.AuthRequestDto;
import com.ecol.userService.dto.UserResponseDto;
import com.ecol.userService.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "accountBlocked", ignore = true)
    @Mapping(target = "accountDeleted", ignore = true)
    @Mapping(target = "accountSuspended", ignore = true)
    @Mapping(target = "accountLocked", ignore = true)
    @Mapping(target = "otpCode", ignore = true)
    @Mapping(target = "otpCodeVerified", ignore = true)
    @Mapping(target = "otpExpiryDate", ignore = true)
    @Mapping(target = "magicLinkToken", ignore = true)
    @Mapping(target = "forgotPassword", ignore = true)
    @Mapping(target = "resetPassword", ignore = true)
    @Mapping(target = "verifyMagicLinkToken", ignore = true)
    @Mapping(target = "magicLinkExpiryDate", ignore = true)
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(AuthRequestDto dto);
    UserResponseDto toResponseDto(User user);
}
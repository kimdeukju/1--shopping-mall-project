package org.spring.teamproject.dto;

import lombok.*;
import org.spring.teamproject.entity.MemberEntity;
import org.spring.teamproject.role.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MemberDto {
    private Long no;

    @NotBlank(message = "이메일을 입력 해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력 해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;


    @NotBlank(message = "이름을 입력해주세요")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]{3,4}$", message = "정확한 이름을 입력해주세요")
    private String userName;

    @NotBlank(message = "핸드폰 번호를 입력해주세요")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$",message = "정확한 핸드폰번호를입력하세요")
    private String phone;

//    @NotBlank(message = "배송주소를 입력 해주세요")
//    private String address;

    private String zip_code;
    private String homeAddress;
    private String DetailAddress;

    private Role role;

    private LocalDateTime createTime; //생성시에만 적용
    private LocalDateTime updateTime;// 수정 시에 적용
    public MemberDto(MemberEntity memberEntity) {
    }

    public static MemberDto updateMemberDto(MemberEntity memberEntity){

        MemberDto memberDto=new MemberDto();

        memberDto.setNo(memberEntity.getNo());
        memberDto.setEmail(memberEntity.getEmail());
        memberDto.setPassword(memberEntity.getPassword());
//        memberDto.setAddress(memberEntity.getAddress());
        memberDto.setZip_code(memberEntity.getZip_code());
        memberDto.setHomeAddress(memberEntity.getHomeAddress());
        memberDto.setDetailAddress(memberEntity.getDetailAddress());
        memberDto.setUserName(memberEntity.getUserName());
        memberDto.setPhone(memberEntity.getPhone());
        memberDto.setCreateTime(memberEntity.getCreateTime());
        return memberDto;
    }
    public static MemberDto adminUpdateDto(MemberEntity memberEntity){

        MemberDto memberDto=new MemberDto();

        memberDto.setNo(memberEntity.getNo());
        memberDto.setEmail(memberEntity.getEmail());
        memberDto.setPassword(memberEntity.getPassword());
//        memberDto.setAddress(memberEntity.getAddress());

        memberDto.setZip_code(memberEntity.getZip_code());
        memberDto.setHomeAddress(memberEntity.getHomeAddress());
        memberDto.setDetailAddress(memberEntity.getDetailAddress());

        memberDto.setUserName(memberEntity.getUserName());
        memberDto.setPhone(memberEntity.getPhone());
        memberDto.setRole(memberEntity.getRole());
        memberDto.setCreateTime(memberEntity.getCreateTime());
        return memberDto;
    }

}

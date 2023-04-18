package org.spring.teamproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.teamproject.dto.MemberDto;
import org.spring.teamproject.role.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long no;

    @Column(nullable = false,unique = true)
    private String email;

    private String password;
//    private String address;

    private String zip_code;
    private String homeAddress;
    private String DetailAddress;

    private String userName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    //관리자
    @OneToMany(mappedBy = "item_member",cascade = CascadeType.REMOVE)
    private List<ItemEntity> itemEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardJoinMember",cascade = CascadeType.REMOVE)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

//    @OneToMany(mappedBy = "reply_member",cascade = CascadeType.REMOVE)
//    private List<ReplyEntity> replyEntityList= new ArrayList<>();
    //@OneToOne
    //text

    public static MemberEntity memberEntity(MemberDto memberDto,
                                            PasswordEncoder passwordEncoder){
        MemberEntity memberEntity=new MemberEntity();

        memberEntity.setNo(memberDto.getNo());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setPassword(passwordEncoder.encode(memberDto.getPassword()));
//        memberEntity.setAddress(memberDto.getAddress());

        memberEntity.setZip_code(memberDto.getZip_code());
        memberEntity.setHomeAddress(memberDto.getHomeAddress());
        memberEntity.setDetailAddress(memberDto.getDetailAddress());
        memberEntity.setUserName(memberDto.getUserName());
        memberEntity.setPhone(memberDto.getPhone());
        memberEntity.setRole(Role.MEMBER);

        return memberEntity;
    }

    public static MemberEntity adminEntity(MemberDto memberDto,
                                            PasswordEncoder passwordEncoder){
        MemberEntity memberEntity=new MemberEntity();

        memberEntity.setNo(memberDto.getNo());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setPassword(passwordEncoder.encode(memberDto.getPassword()));
//        memberEntity.setAddress(memberDto.getAddress());
        memberEntity.setZip_code(memberDto.getZip_code());
        memberEntity.setHomeAddress(memberDto.getHomeAddress());
        memberEntity.setDetailAddress(memberDto.getDetailAddress());
        memberEntity.setUserName(memberDto.getUserName());
        memberEntity.setPhone(memberDto.getPhone());
        memberEntity.setRole(Role.ADMIN);

        return memberEntity;
    }

    public static MemberEntity updateMemberEntity(MemberDto memberDto,PasswordEncoder passwordEncoder){
        MemberEntity memberEntity=new MemberEntity();
        //id정보
        memberEntity.setNo(memberDto.getNo());
        memberEntity.setEmail(memberDto.getEmail());
        memberEntity.setPassword(passwordEncoder.encode(memberDto.getPassword()));
//        memberEntity.setAddress(memberDto.getAddress());
        memberEntity.setZip_code(memberDto.getZip_code());
        memberEntity.setHomeAddress(memberDto.getHomeAddress());
        memberEntity.setDetailAddress(memberDto.getDetailAddress());
        memberEntity.setUserName(memberDto.getUserName());
        memberEntity.setPhone(memberDto.getPhone());
        memberEntity.setRole(Role.MEMBER);
        return memberEntity;
    }
}

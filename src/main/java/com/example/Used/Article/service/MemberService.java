package com.example.Used.Article.service;
import com.example.Used.Article.domain.Member;
import com.example.Used.Article.exception.NotEnoughStockException;
import com.example.Used.Article.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member createMember(String username, String email,String address, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setAddress(address);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return member;
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.getMemberByUsername(username);
    }

    @Transactional
    public Member getUser(String username) {
        List<Member> member = memberRepository.findByName(username);
        if (!member.isEmpty()) {
            return member.get(0);
        } else {
            throw new NotEnoughStockException("사용자를 찾을 수 없습니다.");
        }
    }

    @Transactional
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

}

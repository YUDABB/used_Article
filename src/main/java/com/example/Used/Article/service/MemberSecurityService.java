package com.example.Used.Article.service;

import com.example.Used.Article.domain.Member;
import com.example.Used.Article.domain.MemberState;
import com.example.Used.Article.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MemberSecurityService  implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> member = this.memberRepository.findByName(username);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        Member members = member.get(0);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(MemberState.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberState.USER.getValue()));
        }
        return new User(members.getUsername(), members.getPassword(), authorities);
    }
}

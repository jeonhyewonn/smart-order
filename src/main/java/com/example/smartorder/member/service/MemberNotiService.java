package com.example.smartorder.member.service;

import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.order.adapter.publisher.dto.DeliverOrderMessage;
import com.example.smartorder.order.adapter.publisher.dto.RejectionOrderMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberNotiService {
    private final MemberRepository memberRepository;

    public void notifyToPickup(DeliverOrderMessage deliverOrderMessage) {
        Member member = this.memberRepository.findById(deliverOrderMessage.getMemberId())
                .orElseThrow(NotFoundMemberException::new);

        // TODO: PUSH 메시지 날리기 state 변경 사항은 없음
    }

    public void notifyRejection(RejectionOrderMessage rejectionOrderMessage) {
        Member member = this.memberRepository.findById(rejectionOrderMessage.getMemberId())
                .orElseThrow(NotFoundMemberException::new);

        // TODO: PUSH 메시지 날리기 state 변경 사항은 없음
    }
}

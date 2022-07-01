package com.example.smartorder.member.service;

import com.example.smartorder.member.MemberMock;
import com.example.smartorder.member.domain.Member;
import com.example.smartorder.member.exception.NotFoundMemberException;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.order.OrderMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

class MemberNotiServiceTest {
    private final MemberRepository memberRepository;
    private final MemberNotiService memberNotiService;
    private OrderMock orderMock;
    private Member member;

    public MemberNotiServiceTest() {
        this.memberRepository = Mockito.mock(MemberRepository.class);
        this.memberNotiService = new MemberNotiService(
                this.memberRepository
        );
    }

    @BeforeEach
    public void beforeEach() {
        this.orderMock = new OrderMock();
        this.member = new MemberMock().member;
    }

    @Test
    void notifyToPickupThrowNotFoundMember() {
        // Given
        Long memberId = 0L;
        when(this.memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> {
            this.memberNotiService.notifyToPickup(this.orderMock.deliverOrderMessage);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    void notifyToPickupWillSucceed() {
        // Given
        Long memberId = this.orderMock.deliverOrderMessage.getMemberId();
        when(this.memberRepository.findById(memberId)).thenReturn(Optional.of(this.member));

        // When
        this.memberNotiService.notifyToPickup(this.orderMock.deliverOrderMessage);

        // Then
        // TODO: PUSH 알람 보냈는지 확인
    }

    @Test
    void notifyRejectionThrowNotFoundMember() {
        // Given
        Long memberId = 0L;
        when(this.memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> {
            this.memberNotiService.notifyRejection(this.orderMock.rejectionOrderMessage);
        }).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    void notifyRejectionWillSucceed() {
        // Given
        Long memberId = this.orderMock.rejectionOrderMessage.getMemberId();
        when(this.memberRepository.findById(memberId)).thenReturn(Optional.of(this.member));

        // When
        this.memberNotiService.notifyRejection(this.orderMock.rejectionOrderMessage);

        // Then
        // TODO: PUSH 알람 보냈는지 확인
    }
}
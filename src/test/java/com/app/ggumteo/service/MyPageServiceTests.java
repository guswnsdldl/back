package com.app.ggumteo.service;

import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.admin.AdminAnswerDTO;
import com.app.ggumteo.domain.buy.*;
import com.app.ggumteo.domain.funding.*;
import com.app.ggumteo.domain.inquiry.InquiryDTO;
import com.app.ggumteo.domain.inquiry.MyInquiryHistoryListDTO;
import com.app.ggumteo.domain.member.MemberDTO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.work.MyWorkListDTO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.pagination.SettingTablePagination;
import com.app.ggumteo.pagination.WorkAndFundingPagination;
import com.app.ggumteo.repository.inquiry.InquiryDAO;
import com.app.ggumteo.service.myPage.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j

public class MyPageServiceTests {
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private InquiryDAO inquiryDAO;

    @Test
    public void testGetMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(1L);

        Optional<MemberVO> foundMemberInfo = myPageService.getMember(memberDTO.getId());
        foundMemberInfo.map(MemberVO::toString).ifPresent(log::info);
    }

    @Test
    public void testGetMyVideoWorkList() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(15L).get();
        workAndFundingPagination.setTotal(myPageService.getMyVideoWorkPostsTotal(memberVO.getId(), PostType.WORKVIDEO.name()));
        workAndFundingPagination.progress();
        MyWorkListDTO myWorkPosts = myPageService.getMyVideoWorkList
                (1, workAndFundingPagination, memberVO.getId(), PostType.WORKVIDEO.name());

        log.info(" myWorkPosts.toString()-test={}", myWorkPosts.toString());
    }

    @Test
    public void testGetMyVideoWorkBuyerList() {
        WorkDTO workDTO = null;
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        workDTO = myPageService.getWork(38L, PostType.WORKVIDEO.name()).get();
        settingTablePagination.setTotal(myPageService.getMyVideoWorkBuyersTotal(workDTO.getId()));
        settingTablePagination.progress();
        MyWorkBuyerListDTO myWorkBuyers = myPageService.getMyVideoWorkBuyerList(1, settingTablePagination, workDTO.getId());

        log.info("myWorkBuyers.toString()-test={}", myWorkBuyers.toString());
    }

    @Test
    public void testUpdateWorkSendStatus() {
        BuyWorkDTO buyWorkDTO = new BuyWorkDTO();
        buyWorkDTO.setId(1L);
        buyWorkDTO.setWorkSendStatus("YES");

        myPageService.updateWorkSendStatus(buyWorkDTO.toVO());
    }

    @Test
    public void testGetMyFundingPosts() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyFundingPostsTotal(memberVO.getId(), PostType.FUNDINGVIDEO.name()));
        workAndFundingPagination.progress();
        MyFundingListDTO myFundingPosts = myPageService.getMyVideoFundingList(1, workAndFundingPagination, memberVO.getId(), PostType.FUNDINGVIDEO.name());

        log.info("GetMyFundingPosts-test={}", myFundingPosts.toString());
    }

    @Test
    public void testGetMyFundingBuyerList() {
        FundingDTO fundingDTO = null;
        SettingTablePagination settingTablePagination = new SettingTablePagination();
        fundingDTO = myPageService.getFunding(9L, PostType.FUNDINGVIDEO.name()).get();
        settingTablePagination.setTotal(myPageService.getMyFundingPostBuyersTotal(fundingDTO.getId()));
        settingTablePagination.progress();
        MyFundingBuyerListDTO myFundingBuyers = myPageService.getMyFundingBuyerList(1, settingTablePagination, fundingDTO.getId());

        log.info("GetMyFundingBuyerList-test={}", myFundingBuyers.toString());
  }

    @Test
    public void testUpdateFundingSendStatus() {
    BuyFundingProductDTO buyFundingProductDTO = new BuyFundingProductDTO();
    buyFundingProductDTO.setId(7L);
    buyFundingProductDTO.setFundingSendStatus("YES");

    myPageService.updateFundingSendStatus(buyFundingProductDTO.toVO());
    }

    @Test
    public void testGetFundingListPaidByMemberTotal() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyBuyFundingListTotal(memberVO.getId(), PostType.FUNDINGVIDEO.name()));
        workAndFundingPagination.progress();
        MyBuyFundingListDTO fundingPostsPaidByMember = myPageService
                .getMyBuyFundingList(1, workAndFundingPagination, memberVO.getId(), PostType.FUNDINGVIDEO.name());

        log.info(fundingPostsPaidByMember.toString());
    }

    @Test
    public void testGetMyInquiryHistoryList() {
        MemberVO memberVO = null;
        WorkAndFundingPagination workAndFundingPagination = new WorkAndFundingPagination();
        memberVO = myPageService.getMember(1L).get();
        workAndFundingPagination.setTotal(myPageService.getMyInquiryHistoriesTotal(memberVO.getId()));
        workAndFundingPagination.progress();
        MyInquiryHistoryListDTO myInquiryHistories = myPageService.getMyInquiryHistoryList(1, workAndFundingPagination, memberVO.getId());

        log.info(myInquiryHistories.toString());
    }

    @Test
    public void testGetAdminAnswerByInquiryId() {
        InquiryDTO inquiryDTO = null;
        inquiryDTO = myPageService.getInquiry(20L).get();
        log.info(inquiryDTO.toString());
        myPageService.getAdminAnswerByInquiryId(inquiryDTO.getPostId())
                .stream().map(AdminAnswerDTO::toString).forEach(log::info);
    }

}

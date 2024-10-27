package com.app.ggumteo.controller.work;



import com.app.ggumteo.constant.PostType;
import com.app.ggumteo.domain.file.FileVO;
import com.app.ggumteo.domain.file.PostFileDTO;
import com.app.ggumteo.domain.file.PostFileVO;
import com.app.ggumteo.domain.member.MemberVO;
import com.app.ggumteo.domain.post.PostVO;
import com.app.ggumteo.domain.work.WorkDTO;
import com.app.ggumteo.domain.work.WorkVO;
import com.app.ggumteo.mapper.post.PostMapper;
import com.app.ggumteo.mapper.work.WorkMapper;
import com.app.ggumteo.service.file.PostFileService;
import com.app.ggumteo.service.work.WorkService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/text/*")
@RequiredArgsConstructor
@Slf4j
public class TextWorkController {
    private final WorkService workService;
    private final HttpSession session;
    private final PostFileService postFileService;

    // 모든 요청 전 세션에 테스트용 사용자 정보 설정
    @ModelAttribute
    public void setTestMember(HttpSession session) {
        if (session.getAttribute("member") == null) {
            session.setAttribute("member", new MemberVO(
                    2L,
                    "",      // memberName
                    "",         // memberEmail
                    "",
                    "",              // profileUrl
                    "",          // createdDate
                    ""          // updatedDate
            ));
        }
    }

    @GetMapping("write")
    public void goToWriteForm() {;}

    @PostMapping("write")
    public ResponseEntity<?> write(WorkDTO workDTO, @RequestParam("workFile") MultipartFile[] workFiles,
                                   @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        try {
            MemberVO member = (MemberVO) session.getAttribute("member");
            if (member == null) {
                log.error("세션에 멤버 정보가 없습니다.");
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "세션에 멤버 정보가 없습니다."));
            }

            // PostType을 TEXT로 고정
            workDTO.setPostType(PostType.TEXT.name());
            workDTO.setMemberId(member.getId());

            // Work와 Post 저장
            workService.write(workDTO);

            // 파일 저장 로직 호출 (workFile 및 thumbnailFile 저장)
            if (workFiles != null && workFiles.length > 0) {
                for (MultipartFile file : workFiles) {
                    if (!file.isEmpty()) {
                        postFileService.saveFile(file, workDTO.getId()); // 각각의 파일 저장
                    }
                }
            }
            if (!thumbnailFile.isEmpty()) {
                postFileService.saveFile(thumbnailFile, workDTO.getId());
            }

            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            log.error("글 저장 중 오류 발생", e);
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "저장 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("list")
    public String list(@RequestParam(required = false) String genreType, Model model) {
        try {
            List<WorkDTO> works = workService.findAllWithThumbnail(genreType);
            works.forEach(work -> {
                log.info("Work ID: {}, Thumbnail Path: {}", work.getId(), work.getThumbnailFilePath());
            });
            model.addAttribute("works", works);
            return "/text/list";  // list.html로 이동
        } catch (Exception e) {
            log.error("작품 목록을 불러오는 중 오류 발생", e);
            return "error";  // 오류 발생 시 오류 페이지로 이동
        }
    }




}
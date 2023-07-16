package com.ColdPitch.domain.apicontroller;

import com.ColdPitch.domain.entity.dto.user.CompanyResponseDto;
import com.ColdPitch.domain.entity.dto.usertag.TagResponseDto;
import com.ColdPitch.domain.entity.dto.usertag.TagRequestDto;
import com.ColdPitch.domain.service.UserService;
import com.ColdPitch.domain.service.UserTagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/usertag")
public class UserTagController {
    private final UserService userService;
    private final UserTagService userTagService;

    @PostMapping
    @Operation(summary = "유저 - 테그 등록, 유저 테그 정보를 ENUM으로 받아온다. 로그인 되어있다는 가정")
    public ResponseEntity<List<TagResponseDto>> saveUserTag(@Valid @RequestBody TagRequestDto TagRequestDto) {
        return ResponseEntity.status(200).body(userTagService.setTag(TagRequestDto, userService.getMemberWithAuthorities().orElseThrow(IllegalAccessError::new)));
    }

    @GetMapping
    @Operation(summary = "자신의 유저 테그 조회 ")
    public ResponseEntity<List<TagResponseDto>> findMyTag() {
        return ResponseEntity.status(200).body(userTagService.findMyTag(userService.getMemberWithAuthorities().orElseThrow(IllegalAccessError::new)));
    }


    @PostMapping("/tagname")
    @Operation(summary = "찾고자 하는 테그를 입력하면 해당 태그를 가지고 있는 모든 회사를 찾아준다.(or 조건)")
    public ResponseEntity<List<CompanyResponseDto>> findCompanyByUserTag(@Valid @RequestBody TagRequestDto tagRequestDto) {
        return ResponseEntity.status(200).body(userTagService.findCompanyByEachAllTags(tagRequestDto));
    }
}

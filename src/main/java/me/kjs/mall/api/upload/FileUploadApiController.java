package me.kjs.mall.api.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.connect.ConnectService;
import me.kjs.mall.connect.UploadPath;
import me.kjs.mall.connect.exception.FileUploadFailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/upload")
public class FileUploadApiController {

    private final ConnectService connectService;

    @PostMapping("/exchange")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto exchangeImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.EXCHANGE);

        return ResponseDto.created(path);
    }


    @PostMapping("/base")
    @PreAuthorize("hasRole('ROLE_BASE_PRODUCT')")
    public ResponseDto fileImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.BASE_PRODUCT);

        return ResponseDto.created(path);
    }

    @PostMapping("/review")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto reviewImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.REVIEW);

        return ResponseDto.created(path);
    }

    @PostMapping("/event")
    @PreAuthorize("hasRole('ROLE_EVENT')")
    public ResponseDto eventImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.EVENT);

        return ResponseDto.created(path);
    }

    @PostMapping("/notice")
    @PreAuthorize("hasRole('ROLE_NOTICE')")
    public ResponseDto noticeImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.NOTICE);

        return ResponseDto.created(path);
    }

    @PostMapping("/story")
    @PreAuthorize("hasRole('ROLE_STORY')")
    public ResponseDto storyImageUpload(@RequestParam("file") MultipartFile file) {
        if (!validation(file)) {
            throw new FileUploadFailException();
        }
        String path = connectService.uploadImage(file, UploadPath.STORY);

        return ResponseDto.created(path);
    }


    public boolean validation(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        int pos = originalFileName.lastIndexOf(".");
        String fileExt = originalFileName.substring(pos + 1);
        switch (fileExt.toUpperCase()) {
            case "BMP":
            case "RLE":
            case "DIB":
            case "JPEG":
            case "JPG":
            case "GIF":
            case "PNG":
            case "TIF":
            case "TIFF":
            case "RAW":
                return true;
        }
        return false;
    }
}

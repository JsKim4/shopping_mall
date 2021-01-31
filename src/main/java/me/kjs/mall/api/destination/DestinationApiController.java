package me.kjs.mall.api.destination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.destination.Destination;
import me.kjs.mall.destination.DestinationService;
import me.kjs.mall.destination.dto.DestinationDto;
import me.kjs.mall.destination.dto.DestinationSaveDto;
import me.kjs.mall.member.Member;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destinations")
@Slf4j
@PreAuthorize("hasRole('ROLE_USER')")
public class DestinationApiController {
    private final DestinationService destinationService;

    @PostMapping
    public ResponseDto createDestination(
            @RequestBody @Validated DestinationSaveDto destinationSaveDto,
            @CurrentMember Member member,
            Errors errors) {
        hasErrorsThrow(errors);
        Destination destination = destinationService.createDestination(destinationSaveDto, member.getId());

        DestinationDto destinationDto = DestinationDto.destinationToDto(destination);
        return ResponseDto.created(destinationDto);
    }

    @PutMapping("/{destinationId}")
    public ResponseDto updateDestination(
            @RequestBody DestinationSaveDto destinationSaveDto,
            @CurrentMember Member member,
            @PathVariable("destinationId") Long destinationId) {

        destinationService.updateDestination(member.getId(), destinationId, destinationSaveDto);

        return ResponseDto.noContent();
    }

    @DeleteMapping("/{destinationId}")
    public ResponseDto deleteDestination(
            @CurrentMember Member member,
            @PathVariable("destinationId") Long destinationId) {

        destinationService.removeDestination(member.getId(), destinationId);

        return ResponseDto.noContent();
    }

    @GetMapping
    public ResponseDto findDestinationDyMember(@CurrentMember Member member) {
        List<Destination> destinations = destinationService.findDestinationByMemberId(member.getId());
        List<DestinationDto> body = destinations.stream().map(DestinationDto::destinationToDto).collect(Collectors.toList());

        return ResponseDto.ok(body);
    }

}

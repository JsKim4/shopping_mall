package me.kjs.mall.api.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ValidationErrorException;
import me.kjs.mall.partial.NewProductCalendar;
import me.kjs.mall.partial.NewProductQueryRepository;
import me.kjs.mall.partial.PartialService;
import me.kjs.mall.partial.dto.NewProductCalendarCreateDto;
import me.kjs.mall.partial.dto.NewProductCalendarDto;
import me.kjs.mall.partial.dto.NewProductCreateDto;
import me.kjs.mall.partial.dto.NewProductSearchCondition;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSimpleDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/new/product")
@RequiredArgsConstructor
@Slf4j
public class NewProductCalendarApiController {

    private final PartialService partialService;
    private final NewProductQueryRepository newProductQueryRepository;

    @PostMapping("/calendar/{newProductCalendarId}")
    @PreAuthorize("hasRole('ROLE_PRODUCT')")
    public ResponseDto updateCalendar(@PathVariable("newProductCalendarId") Integer newProductCalendarId,
                                      @RequestBody @Validated NewProductCalendarCreateDto newProductCalendarCreateDto,
                                      Errors errors) {
        hasErrorsThrow(errors);
        if (newProductCalendarId < 0 || newProductCalendarId > 10) {
            throw new ValidationErrorException(errors);
        }
        validation(newProductCalendarCreateDto, errors);
        hasErrorsThrow(errors);
        NewProductCalendar newProductCalendar = partialService.updateNewProductCalendar(newProductCalendarId, newProductCalendarCreateDto);
        NewProductCalendarDto newProductCalendarDto = NewProductCalendarDto.newProductCalendarToDto(newProductCalendar);
        return ResponseDto.created(newProductCalendarDto);
    }

    @GetMapping("/calendar")
    @PreAuthorize("hasRole('ROLE_PRODUCT')")
    public ResponseDto queryNewProductCalendar() {

        List<NewProductCalendar> result = partialService.findAllNewProductCalendarFetch();

        List<NewProductCalendarDto> body = result.stream().map(NewProductCalendarDto::newProductCalendarToDto).collect(Collectors.toList());

        return ResponseDto.ok(body);
    }

    @GetMapping("/now")
    public ResponseDto queryNowNewProduct(@Validated NewProductSearchCondition newProductSearchCondition,
                                          Errors errors) {
        hasErrorsThrow(errors);
        CommonPage<Product> result = newProductQueryRepository.findByNowDateAndSearchCondition(LocalDate.now(), newProductSearchCondition);
        CommonPage body = result.updateContent(result.getContents().stream().map(ProductSimpleDto::productToSimpleDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }


    private void validation(NewProductCalendarCreateDto newProductCalendarCreateDto, Errors errors) {
        for (NewProductCreateDto newProduct : newProductCalendarCreateDto.getNewProducts()) {
            for (NewProductCreateDto product : newProductCalendarCreateDto.getNewProducts()) {
                if (newProduct == product) {
                    continue;
                }
                if (newProduct.getBeginDate().equals(product.getBeginDate())) {
                    errors.rejectValue("newProducts", "wrong value", "newProducts can't overlap date");
                }
                if (newProduct.getEndDate().equals(product.getEndDate())) {
                    errors.rejectValue("newProducts", "wrong value", "newProducts can't overlap date");
                }
                if (newProduct.getBeginDate().isBefore(product.getBeginDate())) {
                    if (newProduct.getEndDate().isAfter(product.getBeginDate())) {
                        errors.rejectValue("newProducts", "wrong value", "newProducts can't overlap date");
                    }
                }
                if (newProduct.getBeginDate().isBefore(product.getEndDate())) {
                    if (newProduct.getEndDate().isAfter(product.getEndDate())) {
                        errors.rejectValue("newProducts", "wrong value", "newProducts can't overlap date");
                    }
                }
            }
        }
    }
}

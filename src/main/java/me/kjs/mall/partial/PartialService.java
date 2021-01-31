package me.kjs.mall.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.partial.dto.NewProductCalendarCreateDto;
import me.kjs.mall.partial.dto.NewProductCreateDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PartialService {

    private final NewProductCalendarRepository newProductCalendarRepository;
    private final ProductRepository productRepository;

    @Transactional
    public NewProductCalendar updateNewProductCalendar(Integer newProductCalendarId, NewProductCalendarCreateDto newProductCalendarCreateDto) {
        NewProductCalendar newProductCalendar = newProductCalendarRepository.findById(newProductCalendarId).orElse(NewProductCalendar.initialize(newProductCalendarId));
        newProductCalendar.init();
        for (NewProductCreateDto newProduct : newProductCalendarCreateDto.getNewProducts()) {
            Product product = productRepository.findById(newProduct.getProductId()).orElseThrow(NoExistIdException::new);
            newProductCalendar.addNewProduct(product, newProduct.getBeginDate(), newProduct.getEndDate());
        }
        return newProductCalendarRepository.save(newProductCalendar).loading();
    }

    public List<NewProductCalendar> findAllNewProductCalendarFetch() {
        List<NewProductCalendar> result = newProductCalendarRepository.findAll();
        for (NewProductCalendar newProductCalendar : result) {
            newProductCalendar.loading();
        }
        return result;
    }
}

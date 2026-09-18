package com.cbarkinozer.onlinebankingrestapi.app.crd.mapper;

import com.cbarkinozer.onlinebankingrestapi.app.crd.dto.CrdCreditCardActivityDto;
import com.cbarkinozer.onlinebankingrestapi.app.crd.dto.CrdCreditCardDto;
import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCard;
import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCardActivity;
import com.cbarkinozer.onlinebankingrestapi.app.gen.util.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CrdCreditCardMapper {

    CrdCreditCardMapper INSTANCE = Mappers.getMapper(CrdCreditCardMapper.class);

    List<CrdCreditCardDto> convertToCrdCreditCardDtoList(List<CrdCreditCard> crdCreditCardList);

    List<CrdCreditCardActivityDto> convertToCrdCreditCardActivityDtoList(List<CrdCreditCardActivity> crdCreditCardActivityList);

    @Mapping(target = "cardNo", source = "cardNo", qualifiedByName = "maskCardNo")
    CrdCreditCardDto convertToCrdCreditCardDto(CrdCreditCard crdCreditCard);

    @Named("maskCardNo")
    static String maskCardNo(Long cardNo){
        return StringUtil.maskCardNo(cardNo);
    }

    CrdCreditCardActivityDto convertToCrdCreditCardActivityDto(CrdCreditCardActivity crdCreditCardActivity);
}

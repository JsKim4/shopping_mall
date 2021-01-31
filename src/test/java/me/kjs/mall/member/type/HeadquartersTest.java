package me.kjs.mall.member.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Headquarters Class is")
class HeadquartersTest {

    @Nested
    @DisplayName("getCompanyRankList Method")
    class get_company_rank_list {
        @Test
        @DisplayName("경영관리 본부는 다음과 같은 직급을 가질 수 있다.")
        void getBusinessManagerCompanyRankList() {
            List<CompanyRank> list = Headquarters.BUSINESS_MANAGEMENT.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIVISION_MANAGER));
        }

        @Test
        @DisplayName("IT 본부는 다음과 같은 직급을 가질 수 있다.")
        void getInformationTechnologyCompanyRankList() {
            List<CompanyRank> list = Headquarters.INFORMATION_TECHNOLOGY.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIVISION_MANAGER));
        }

        @Test
        @DisplayName("마케팅 본부는 다음과 같은 직급을 가질 수 있다.")
        void getMarketingCompanyRankList() {
            List<CompanyRank> list = Headquarters.MARKETING.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIVISION_MANAGER));
        }

        @Test
        @DisplayName("고객관리 본부는 다음과 같은 직급을 가질 수 있다.")
        void getCustomerManagementCompanyRankList() {
            List<CompanyRank> list = Headquarters.CUSTOMER_MANAGEMENT.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIVISION_MANAGER));
        }

        @Test
        @DisplayName("연구소는 다음과 같은 직급을 가질 수 있다.")
        void getLaboratoryCompanyRankList() {
            List<CompanyRank> list = Headquarters.LABORATORY.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.RESEARCH_DIRECTOR));
        }

        @Test
        @DisplayName("상품운영본부 다음과 같은 직급을 가질 수 있다.")
        void getProductOperationCompanyRankList() {
            List<CompanyRank> list = Headquarters.PRODUCT_OPERATION.getCompanyRankList();
            assertEquals(list.size(), 7);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIRECTOR));
            assertTrue(list.contains(CompanyRank.AUDITING_DIRECTOR));
        }

        @Test
        @DisplayName("인테리어 본부는 다음과 같은 직급을 가질 수 있다.")
        void getInteriorCompanyRankList() {
            List<CompanyRank> list = Headquarters.INTERIOR.getCompanyRankList();
            assertEquals(list.size(), 6);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIRECTOR));
        }

        @Test
        @DisplayName("영업 본부는 다음과 같은 직급을 가질 수 있다.")
        void getSalesCompanyRankList() {
            List<CompanyRank> list = Headquarters.SALES.getCompanyRankList();
            assertEquals(list.size(), 7);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.MANAGING_DIRECTOR));
            assertTrue(list.contains(CompanyRank.SITE_MANAGER));
        }

        @Test
        @DisplayName("MNC본부는 다음과 같은 직급을 가질 수 있다.")
        void getMNCCompanyRankList() {
            List<CompanyRank> list = Headquarters.MEDICAL_NUTRITION_COUNSEL.getCompanyRankList();
            assertEquals(list.size(), 7);
            commonCompanyRank(list);
            assertTrue(list.contains(CompanyRank.DIRECTOR));
            assertTrue(list.contains(CompanyRank.PART_MANAGER));
        }


        private void commonCompanyRank(List<CompanyRank> list) {
            assertTrue(list.contains(CompanyRank.ASSISTANT_MANAGER));
            assertTrue(list.contains(CompanyRank.ADMINISTRATIVE_MANAGER));
            assertTrue(list.contains(CompanyRank.SECTION_MANAGER));
            assertTrue(list.contains(CompanyRank.TEAM_MANAGER));
            assertTrue(list.contains(CompanyRank.DEPARTMENT_MANAGER));
        }

    }
}
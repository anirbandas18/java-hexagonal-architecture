package com.teenthofabud.demo.hexagonal.architecture.cookbook;

import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.form.PatchOperationForm;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineRecord;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.CuisineJPARepository;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CreateCuisineRequest;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineDetailsResponse;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.error.CookbookErrorCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CuisineIntegrationTest extends CookbookIntegrationBaseTest {

    private static final String CUISINE_URI = "/cuisine";
    private static final String CUISINE_URI_BY_ID = "/cuisine/{id}";

    private CuisineJPARepository cuisineRepository;

    private int integrationServicePort;

    @Autowired
    public void setCuisineRepository(CuisineJPARepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    private CreateCuisineRequest createCuisineRequest;

    private CuisineDetailsResponse cuisineDetailsResponse1;
    private CuisineDetailsResponse cuisineDetailsResponse2;
    private CuisineDetailsResponse cuisineDetailsResponse3;
    private CuisineDetailsResponse cuisineDetailsResponse4;
    private CuisineRecord cuisineRecord1;
    private CuisineRecord cuisineRecord2;
    private CuisineRecord cuisineRecord3;
    private CuisineRecord cuisineRecord4;


    private List<PatchOperationForm> patches;

    @BeforeEach
    private void init() {

        createCuisineRequest = new CreateCuisineRequest();
        createCuisineRequest.setName("New Name");
        createCuisineRequest.setDescription("New Description");

        patches = Arrays.asList(
                new PatchOperationForm("replace", "/name", "patched name"),
                new PatchOperationForm("replace", "/description", "patched description"));


        cuisineRecord1 = new CuisineRecord();
        cuisineRecord1.setName("Cuisine 1 Name");
        cuisineRecord1.setDescription("Cuisine 1 Description");
        cuisineRecord1.setActive(Boolean.TRUE);

        cuisineRecord1 = cuisineRepository.save(cuisineRecord1);

        cuisineDetailsResponse1 = new CuisineDetailsResponse();
        cuisineDetailsResponse1.setId(cuisineRecord1.getId().toString());
        cuisineDetailsResponse1.setName(cuisineRecord1.getName());
        cuisineDetailsResponse1.setDescription(cuisineRecord1.getDescription());

        cuisineRecord2 = new CuisineRecord();
        cuisineRecord2.setName("Cuisine 2 Name");
        cuisineRecord2.setDescription("Cuisine 2 Description");
        cuisineRecord2.setActive(Boolean.TRUE);

        cuisineRecord2 = cuisineRepository.save(cuisineRecord2);

        cuisineDetailsResponse2 = new CuisineDetailsResponse();
        cuisineDetailsResponse2.setId(cuisineRecord2.getId().toString());
        cuisineDetailsResponse2.setName(cuisineRecord2.getName());
        cuisineDetailsResponse2.setDescription(cuisineRecord2.getDescription());

        cuisineRecord3 = new CuisineRecord();
        cuisineRecord3.setName("Cuisine 3 Name");
        cuisineRecord3.setDescription("Cuisine 3 Description");
        cuisineRecord3.setActive(Boolean.FALSE);

        cuisineRecord3 = cuisineRepository.save(cuisineRecord3);

        cuisineDetailsResponse3 = new CuisineDetailsResponse();
        cuisineDetailsResponse3.setId(cuisineRecord3.getId().toString());
        cuisineDetailsResponse3.setName(cuisineRecord3.getName());
        cuisineDetailsResponse3.setDescription(cuisineRecord3.getDescription());

        cuisineRecord4 = new CuisineRecord();
        cuisineRecord4.setName("Cuisine 4 Name");
        cuisineRecord4.setDescription("Cuisine 4 Description");
        cuisineRecord4.setActive(Boolean.FALSE);

        cuisineRecord4 = cuisineRepository.save(cuisineRecord4);

        cuisineDetailsResponse4 = new CuisineDetailsResponse();
        cuisineDetailsResponse4.setId(cuisineRecord4.getId().toString());
        cuisineDetailsResponse4.setName(cuisineRecord4.getName());
        cuisineDetailsResponse4.setDescription(cuisineRecord4.getDescription());

        cuisineRecord1 = cuisineRepository.save(cuisineRecord1);
        cuisineRecord2 = cuisineRepository.save(cuisineRecord2);
        cuisineRecord3 = cuisineRepository.save(cuisineRecord3);
    }

    @AfterEach
    private void destroy() {
        cuisineRepository.deleteById(cuisineRecord1.getId());
        cuisineRepository.deleteById(cuisineRecord2.getId());
        cuisineRepository.deleteById(cuisineRecord3.getId());
        cuisineRepository.deleteById(cuisineRecord4.getId());
    }


    @Test
    public void test_Cuisine_Post_ShouldReturn_201Response_And_NewCuisineId_WhenPosted_WithValidCuisineForm() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(post(CUISINE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createCuisineRequest)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void test_Cuisine_Post_ShouldReturn_500Response_And_ErrorCode_TOAB_COMMON_004_WhenRequested_WithEmptyName() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = TOABErrorCode.SYSTEM_INTERNAL_ERROR.getErrorCode();
        String fieldName = "name";
        createCuisineRequest.setName("");

        mvcResult = mockMvc.perform(post(CUISINE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createCuisineRequest)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));

    }

    @Test
    public void test_Cuisine_Post_ShouldReturn_422Response_And_ErrorCode_RES_COOK_003_WhenPosted_WithNoCuisineForm() throws Exception {
        String id = cuisineRecord1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = CookbookErrorCode.COOK_ATTRIBUTE_UNEXPECTED.getErrorCode();
        String fieldName = "form";
        String message = "not provided";

        mvcResult = mockMvc.perform(post(CUISINE_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(message));

    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "r" })
    public void test_Cuisine_Get_ShouldReturn_400Response_And_ErrorCode_RES_COOK_001_WhenRequestedBy_EmptyInvalidId(String id) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = CookbookErrorCode.COOK_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(get(CUISINE_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Cuisine_Get_ShouldReturn_400Response_And_ErrorCode_RES_COOK_002_WhenRequested_ByAbsentId() throws Exception {
        String id = "5";
        MvcResult mvcResult = null;
        String errorCode = CookbookErrorCode.COOK_NOT_FOUND.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(get(CUISINE_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Cuisine_Get_ShouldReturn_200Response_And_DomainDetails_WhenRequested_ById_AndFirstLevel_Cascade() throws Exception {
        String id = cuisineRecord1.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(CUISINE_URI_BY_ID, id)
                        .queryParam("cascadeUntilLevel", TOABCascadeLevel.ONE.getLevelCode()))
                .andDo(print())
                .andReturn();
        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(cuisineDetailsResponse1.getId(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getId());
        Assertions.assertEquals(cuisineDetailsResponse1.getName(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getName());
        Assertions.assertEquals(cuisineDetailsResponse1.getDescription(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getDescription());
    }

    @Test
    public void test_Cuisine_Get_ShouldReturn_200Response_And_DomainDetails_WhenRequested_ById_AndSecondLevel_Cascade() throws Exception {
        String id = cuisineRecord1.getId().toString();
        MvcResult mvcResult = null;
        mvcResult = this.mockMvc.perform(get(CUISINE_URI_BY_ID, id)
                        .queryParam("cascadeUntilLevel", TOABCascadeLevel.TWO.getLevelCode()))
                .andDo(print())
                .andReturn();
        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(cuisineDetailsResponse1.getId(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getId());
        Assertions.assertEquals(cuisineDetailsResponse1.getName(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getName());
        Assertions.assertEquals(cuisineDetailsResponse1.getDescription(), om.readValue(mvcResult.getResponse().getContentAsString(), CuisineDetailsResponse.class).getDescription());
    }

}

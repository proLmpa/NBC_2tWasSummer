package com.example.itwassummer.card.controller;


/*@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false) //데이터에 추가 되기를 원하지 않으면 해당 속성 제거*/
//  implements UserDetailsService
public class CardControllerTest  {

/*  @Autowired
  ObjectMapper mapper;

  @Autowired
  MockMvc mvc;

  private static final String BASE_URL = "/api/cards";

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() throws Exception {

    this.mvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
        .build();
  }

  @Test
  @DisplayName("카드 상세 조회 테스트")
  void getCards() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long cardId = 1L;

    // then
    MvcResult response = mvc.perform(get(BASE_URL + "/" + cardId)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print()).andReturn();

    // mock 객체 변환
    String jsonResult = response.getResponse().getContentAsString();
    JSONParser parser = new JSONParser();
    JSONObject cardObj = (JSONObject) parser.parse(jsonResult);
    Long returnId = (Long) cardObj.get("cardId");
    JSONArray attachArray = (JSONArray) cardObj.get("attachment");
    List<S3FileDto> attachment = new ArrayList<>();
    if (attachArray != null) {
      for (int i = 0; i < attachArray.size(); i++) {
        JSONObject obj = (JSONObject) attachArray.get(i);
        S3FileDto temp = S3FileDto.builder()
            .uploadFileName(obj.get("uploadFileName").toString())
            .originalFileName(obj.get("uploadFileName").toString())
            .uploadFilePath(obj.get("uploadFilePath").toString())
            .uploadFileUrl(obj.get("uploadFileUrl").toString())
            .build();
        attachment.add(temp);
      }
    }

    Assertions.assertEquals(cardId, returnId);
    CardViewResponseDto returnDto = CardViewResponseDto.builder()
        .cardId((Long) cardObj.get("cardId"))
        .attachment(attachment != null ? attachment : null)
        .description((String) cardObj.get("description"))
        .name((String) cardObj.get("name"))
        .parentId((Long) cardObj.get("parentId"))
        .dueDate((String) cardObj.get("dueDate"))
        .build();
    Assertions.assertEquals("카드등록건입니다.", returnDto.getDescription());

  }

  @Test
  @DisplayName("카드 등록 테스트")
  void insertCards() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");
    String name = "예시카드new2";
    Long parentId = Long.valueOf(25);
    String description = "예시카드입니다.";
    LocalDateTime now = LocalDateTime.now();

    String body = mapper.writeValueAsString(
        CardRequestDto.builder()
            .name(name)
            .dueDate(now)
            .parentId(parentId)
            .description(description)
            .build()

    );

    // when
    MockMultipartFile files = new MockMultipartFile("files"
        , "palmtree.png"
        , "multipart/form-data"
        , new FileInputStream(getClass().getResource("/image/palmtree.png").getFile())
    );
    MockMultipartFile request = new MockMultipartFile("requestDto", null, "application/json",
        body.getBytes(StandardCharsets.UTF_8));

    // then
    mvc.perform(MockMvcRequestBuilders
            .multipart(HttpMethod.POST, BASE_URL)
            .file(request)
            .file(files)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print());

  }

  @Test
  @DisplayName("카드 수정 테스트")
  void updateCards() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");
    String name = "예시카드3";
    Long cardId = Long.valueOf(4);
    Long parentId = Long.valueOf(5);
    String description = "예시카드입니다.";
    LocalDateTime now = LocalDateTime.now();

    String body = mapper.writeValueAsString(
        CardRequestDto.builder()
            .name(name)
            .dueDate(now)
            .parentId(parentId)
            .description(description)
            .build()

    );

    // when
    MockMultipartFile files = new MockMultipartFile("files"
        , "pororo.png"
        , "multipart/form-data"
        , new FileInputStream(getClass().getResource("/image/pororo.png").getFile())
    );
    MockMultipartFile request = new MockMultipartFile("requestDto", null, "application/json",
        body.getBytes(StandardCharsets.UTF_8));

    // then
    mvc.perform(MockMvcRequestBuilders
            .multipart(HttpMethod.PUT, BASE_URL + "/" + cardId)
            .file(request)
            .file(files)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("카드 삭제 테스트")
  void deleteCards() throws Exception {
    // given
    // String header = login();
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long cardId = 4L;

    // then
    mvc.perform(delete(BASE_URL + "/" + cardId)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("작업자 변경 테스트")
  void changeCardsUsers() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long cardId = 5L;

    // then
    mvc.perform(patch(BASE_URL + "/" + cardId + "/members")
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
            .param("emailList", "user@email.com,buyer@email.com")
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("정렬순서 변경 테스트")
  void moveCards() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long cardId = 3L;
    Long order = 1L;

    // then
    mvc.perform(patch(BASE_URL + "/" + cardId + "/move")
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
            .param("order", String.valueOf(order))
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("마감일 변경 테스트")
  void changeCardDueDate() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long cardId = 3L;
    LocalDateTime newDueDate = LocalDateTime.now().plusDays(1); //1일 더하여 새로운 마감일 설정

    // then
    mvc.perform(patch(BASE_URL + "/" + cardId + "/date")
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
            .param("dueDate", String.valueOf(newDueDate))
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new User("email", "password", Collections.EMPTY_LIST);
  }*/
}

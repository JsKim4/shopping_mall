create table account_group
(
    account_group_id   numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    alias              nvarchar(255) ,
    name               nvarchar(255),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (account_group_id)
)
create table account_group_roles
(
    account_group_account_group_id numeric(19, 0) not null,
    roles                          nvarchar(255)
)
create table app_version
(
    package_name       nvarchar(255) not null,
    created_date       datetime,
    last_modified_date datetime,
    lately_code        nvarchar(255),
    lately_version     nvarchar(255),
    min_code           nvarchar(255),
    min_version        nvarchar(255),
    os                 nvarchar(255),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (package_name)
)
create table base_product
(
    base_product_id     numeric(19, 0) identity not null,
    created_date        datetime,
    last_modified_date  datetime,
    code                nvarchar(300),
    content             nvarchar(4000),
    name                nvarchar(1000),
    origin_price        int                     not null,
    status              nvarchar(255),
    created_by          numeric(19, 0),
    last_modified_by    numeric(19, 0),
    product_delivery_id numeric(19, 0),
    provision_notice_id numeric(19, 0),
    primary key (base_product_id)
)
create table base_product_product_tags
(
    base_product_base_product_id numeric(19, 0) not null,
    product_tags                 nvarchar(255)
)
create table base_product_thumbnail_image
(
    base_product_base_product_id numeric(19, 0) not null,
    image                        nvarchar(255),
    sort_order                   int            not null
)
create table best_review
(
    best_review_id         numeric(19, 0) identity not null,
    created_date           datetime,
    last_modified_date     datetime,
    created_by             numeric(19, 0),
    last_modified_by       numeric(19, 0),
    best_review_product_id numeric(19, 0),
    review_id              numeric(19, 0),
    primary key (best_review_id)
)
create table best_review_product
(
    best_review_product_id numeric(19, 0) identity not null,
    created_date           datetime,
    last_modified_date     datetime,
    orders                 int                     not null,
    created_by             numeric(19, 0),
    last_modified_by       numeric(19, 0),
    product_id             numeric(19, 0),
    primary key (best_review_product_id)
)
create table best_seller
(
    best_seller_id     numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    date               datetime,
    sales_rate         int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    product_id         numeric(19, 0),
    primary key (best_seller_id)
)
create table cart
(
    cart_id            numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    quantity           int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    member_id          numeric(19, 0),
    product_id         numeric(19, 0),
    primary key (cart_id)
)
create table category
(
    category_id         numeric(19, 0) identity not null,
    created_date        datetime,
    last_modified_date  datetime,
    deletable           nvarchar(255),
    name                nvarchar(255),
    product_containable nvarchar(255),
    status              nvarchar(255),
    created_by          numeric(19, 0),
    last_modified_by    numeric(19, 0),
    parent_category_id  numeric(19, 0),
    primary key (category_id)
)
create table category_product
(
    category_product_id numeric(19, 0) identity not null,
    created_date        datetime,
    last_modified_date  datetime,
    created_by          numeric(19, 0),
    last_modified_by    numeric(19, 0),
    product_id          numeric(19, 0),
    category_id         numeric(19, 0),
    primary key (category_product_id)
)
create table cert
(
    member_cert_id     numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    cert_type          nvarchar(255),
    expired_date_time  datetime,
    key_text           nvarchar(2000),
    token              nvarchar(2000),
    member_id          numeric(19, 0),
    primary key (member_cert_id)
)
create table coupon
(
    coupon_id          numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    content            nvarchar(4000),
    discount_amount    int                     not null,
    discount_type      nvarchar(255),
    max_discount_price int                     not null,
    max_period         int                     not null,
    min_price          int                     not null,
    status             nvarchar(255),
    title              nvarchar(1000),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (coupon_id)
)
create table coupon_issue_condition
(
    coupon_issue_condition_id       numeric(19, 0) identity not null,
    created_date                    datetime,
    last_modified_date              datetime,
    issue_available_begin_date_time datetime,
    issue_available_end_date_time   datetime,
    use_date_time                   bit                     not null,
    created_by                      numeric(19, 0),
    last_modified_by                numeric(19, 0),
    coupon_condition_id             numeric(19, 0),
    primary key (coupon_issue_condition_id)
)
create table destination
(
    destination_id         numeric(19, 0) identity not null,
    created_date           datetime,
    last_modified_date     datetime,
    address_detail         nvarchar(4000),
    address_simple         nvarchar(4000),
    default_destination_yn int,
    name                   nvarchar(255),
    recipient              nvarchar(2000),
    tel1                   nvarchar(1000),
    tel2                   nvarchar(1000),
    zipcode                nvarchar(255),
    created_by             numeric(19, 0),
    last_modified_by       numeric(19, 0),
    member_id              numeric(19, 0),
    primary key (destination_id)
)
create table event
(
    event_id           numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    begin_date_time    datetime,
    end_date_time      datetime,
    status             nvarchar(255),
    thumbnail_image    nvarchar(4000),
    title              nvarchar(255),
    visit_count        int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (event_id)
)
create table event_contents
(
    event_event_id numeric(19, 0) not null,
    contents       nvarchar(4000)
)
create table issue_coupon
(
    issue_coupon_id    numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    coupon_status      nvarchar(255),
    expired_date       datetime,
    status             nvarchar(255),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    coupon_id          numeric(19, 0),
    member_id          numeric(19, 0),
    primary key (issue_coupon_id)
)
create table main_banner
(
    main_banner_id     numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    banner_type        nvarchar(255),
    item_id            numeric(19, 0),
    link               nvarchar(4000),
    mobile_image       nvarchar(4000),
    pc_image           nvarchar(4000),
    sort_number        int                     not null,
    status             nvarchar(255),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (main_banner_id)
)
create table member
(
    member_id                      numeric(19, 0) identity not null,
    created_date                   datetime,
    last_modified_date             datetime,
    account_status                 nvarchar(255),
    birth                          nvarchar(4000),
    email                          nvarchar(4000),
    gender                         nvarchar(255),
    last_login_date_time           datetime,
    last_token_issue_date_time     datetime,
    refresh_token                  nvarchar(4000),
    refresh_token_expire_date_time datetime,
    name                           nvarchar(4000),
    password                       nvarchar(4000),
    phone_number                   nvarchar(4000),
    accumulate_point               numeric(19, 0)          not null,
    points_held                    numeric(19, 0)          not null,
    points_used                    numeric(19, 0)          not null,
    withdraw_date_time             datetime,
    account_group_id               numeric(19, 0),
    primary key (member_id)
)
create table member_ban_history
(
    member_ban_history_id numeric(19, 0) identity not null,
    created_date          datetime,
    last_modified_date    datetime,
    begin_date            datetime,
    cause_message         nvarchar(4000),
    common_status         int,
    end_date              datetime,
    created_by            numeric(19, 0),
    last_modified_by      numeric(19, 0),
    member_id             numeric(19, 0),
    primary key (member_ban_history_id)
)
create table member_policy
(
    member_policy_id   numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    accept_type        nvarchar(255),
    policy_type        nvarchar(255),
    response_date_time datetime,
    member_id          numeric(19, 0),
    primary key (member_policy_id)
)
create table new_product
(
    new_product_id          numeric(19, 0) identity not null,
    created_date            datetime,
    last_modified_date      datetime,
    begin_date_time         datetime,
    end_date_time           datetime,
    created_by              numeric(19, 0),
    last_modified_by        numeric(19, 0),
    new_product_calendar_id int,
    product_id              numeric(19, 0),
    primary key (new_product_id)
)
create table new_product_calendar
(
    new_product_calendar_id int not null,
    primary key (new_product_calendar_id)
)
create table non_member
(
    non_member_id numeric(19, 0) identity not null,
    email         nvarchar(4000),
    name          nvarchar(4000),
    phone_number  nvarchar(4000),
    primary key (non_member_id)
)
create table notice
(
    notice_id          numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    begin_date_time    datetime,
    content            nvarchar(4000),
    status             int,
    thumbnail_image    nvarchar(4000),
    title              nvarchar(4000),
    visit_count        int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (notice_id)
)
create table order_cancel
(
    order_cancel_id            numeric(19, 0) identity not null,
    created_date               datetime,
    last_modified_date         datetime,
    account_name               nvarchar(4000),
    account_no                 nvarchar(4000),
    bank                       int,
    cancel_cause               nvarchar(4000),
    cancel_price               int                     not null,
    created_by                 numeric(19, 0),
    last_modified_by           numeric(19, 0),
    order_id                   numeric(19, 0),
    order_specific_id          numeric(19, 0),
    payment_cancel_request_id  numeric(19, 0),
    payment_cancel_response_id numeric(19, 0),
    primary key (order_cancel_id)
)
create table order_destination
(
    order_destination_id numeric(19, 0) identity not null,
    created_date         datetime,
    last_modified_date   datetime,
    accept_date_time     datetime,
    address              nvarchar(4000),
    address_detail       nvarchar(4000),
    carrier              nvarchar(255),
    doing_date_time      datetime,
    invoice_number       nvarchar(255),
    message              nvarchar(4000),
    recipient            nvarchar(4000),
    tel1                 nvarchar(4000),
    tel2                 nvarchar(4000),
    zipcode              nvarchar(4000),
    created_by           numeric(19, 0),
    last_modified_by     numeric(19, 0),
    primary key (order_destination_id)
)
create table order_exchange
(
    order_exchange_id  numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    exchange_cause     nvarchar(4000),
    receipt_date_time  datetime,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (order_exchange_id)
)
create table order_exchange_cause_image
(
    order_exchange_order_exchange_id numeric(19, 0) not null,
    cause_image                      nvarchar(4000)
)
create table order_exchange_product
(
    order_exchange_product_id numeric(19, 0) identity not null,
    created_date              datetime,
    last_modified_date        datetime,
    quantity                  int                     not null,
    created_by                numeric(19, 0),
    last_modified_by          numeric(19, 0),
    order_exchange_id         numeric(19, 0),
    primary key (order_exchange_product_id)
)
create table order_payment
(
    order_payment_id                  numeric(19, 0) identity not null,
    payment_accept_date_time          datetime,
    payment_code                      nvarchar(255)           not null,
    payment_method                    nvarchar(255),
    total_delivery_fee                int                     not null,
    total_discount_price              int                     not null,
    total_original_price              int                     not null,
    total_payment_price               int                     not null,
    total_price                       int                     not null,
    use_point                         int                     not null,
    payment_approve_request_id        numeric(19, 0),
    payment_approve_response_id       numeric(19, 0),
    payment_certification_request_id  numeric(19, 0),
    payment_certification_response_id numeric(19, 0),
    primary key (order_payment_id)
)
create table order_product
(
    order_product_id             numeric(19, 0) identity not null,
    created_date                 datetime,
    last_modified_date           datetime,
    order_item_code              nvarchar(255),
    order_product_exchange_state nvarchar(255),
    accumulate_expected_point    int                     not null,
    accumulate_point             int                     not null,
    discount_price               int                     not null,
    discount_type                nvarchar(255),
    origin_price                 int                     not null,
    price                        int                     not null,
    quantity                     int                     not null,
    remain_quantity              int                     not null,
    sum_discount_price           int                     not null,
    sum_origin_price             int                     not null,
    sum_price                    int                     not null,
    order_product_state          nvarchar(255),
    created_by                   numeric(19, 0),
    last_modified_by             numeric(19, 0),
    order_exchange_product_id    numeric(19, 0),
    order_specific_id            numeric(19, 0),
    product_id                   numeric(19, 0),
    review_id                    numeric(19, 0),
    primary key (order_product_id)
)
create table order_product_log
(
    order_product_log_id     numeric(19, 0) identity not null,
    created_date             datetime,
    last_modified_date       datetime,
    post_order_product_state nvarchar(255),
    pre_order_product_state  nvarchar(255),
    created_by               numeric(19, 0),
    last_modified_by         numeric(19, 0),
    order_product_id         numeric(19, 0),
    primary key (order_product_log_id)
)
create table orders
(
    order_id           numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    non_member_order   nvarchar(255),
    order_name         nvarchar(4000),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    non_member_id      numeric(19, 0),
    member_id          numeric(19, 0),
    order_payment_id   numeric(19, 0),
    primary key (order_id)
)
create table order_specific
(
    order_specific_id             numeric(19, 0) identity not null,
    created_date                  datetime,
    last_modified_date            datetime,
    accept_state_modify_date_time datetime,
    order_code                    nvarchar(255),
    order_date_time               datetime,
    order_exchange_state          nvarchar(255),
    order_name                    nvarchar(4000),
    cancel_available_price        int                     not null,
    delivery_fee                  int                     not null,
    delivery_free_benefit         nvarchar(255),
    sum_discount_price            int                     not null,
    sum_original_price            int                     not null,
    sum_price                     int                     not null,
    use_point                     int                     not null,
    order_state                   nvarchar(255),
    created_by                    numeric(19, 0),
    last_modified_by              numeric(19, 0),
    order_id                      numeric(19, 0),
    order_destination_id          numeric(19, 0),
    order_exchange_id             numeric(19, 0),
    primary key (order_specific_id)
)
create table payment_approve_request
(
    payment_approve_request_id numeric(19, 0) identity not null,
    created_date               datetime,
    last_modified_date         datetime,
    auth_token                 nvarchar(255),
    edit_date                  datetime,
    payment_price              nvarchar(255),
    sign_data                  nvarchar(255),
    trade_id                   nvarchar(255),
    created_by                 numeric(19, 0),
    last_modified_by           numeric(19, 0),
    primary key (payment_approve_request_id)
)
create table payment_approve_response
(
    payment_approve_response_id numeric(19, 0) identity not null,
    created_date                datetime,
    last_modified_date          datetime,
    accept_date_time            datetime,
    auth_code                   nvarchar(255),
    auth_date                   nvarchar(255),
    goods_name                  nvarchar(255),
    merchant_id                 nvarchar(255),
    pay_method                  nvarchar(255),
    payment_code                nvarchar(255),
    payment_price               nvarchar(255),
    bank_code                   nvarchar(255),
    bank_name                   nvarchar(255),
    bank_num                    nvarchar(255),
    exp_date                    nvarchar(255),
    exp_time                    nvarchar(255),
    result_code                 nvarchar(4000),
    result_message              nvarchar(4000),
    signature                   nvarchar(255),
    created_by                  numeric(19, 0),
    last_modified_by            numeric(19, 0),
    primary key (payment_approve_response_id)
)
create table payment_cancel_request
(
    payment_cancel_id   numeric(19, 0) identity not null,
    created_date        datetime,
    last_modified_date  datetime,
    account_name        nvarchar(255),
    account_no          nvarchar(255),
    bank_code           nvarchar(255),
    cancel_code         nvarchar(255),
    cancel_message      nvarchar(255),
    cancel_price        int                     not null,
    char_set            nvarchar(255),
    edit_date           datetime,
    merchant_id         nvarchar(255),
    partial_cancel_code nvarchar(255),
    sign_data           nvarchar(255),
    trade_id            nvarchar(255),
    created_by          numeric(19, 0),
    last_modified_by    numeric(19, 0),
    primary key (payment_cancel_id)
)
create table payment_cancel_response
(
    payment_cancel_response_id numeric(19, 0) identity not null,
    created_date               datetime,
    last_modified_date         datetime,
    cancel_code                nvarchar(255),
    cancel_date                nvarchar(255),
    cancel_num                 nvarchar(255),
    cancel_price               nvarchar(255),
    cancel_time                nvarchar(255),
    error_code                 nvarchar(4000),
    error_message              nvarchar(4000),
    merchant_id                nvarchar(255),
    pay_method                 nvarchar(255),
    remain_price               nvarchar(255),
    result_code                nvarchar(4000),
    result_message             nvarchar(4000),
    signature                  nvarchar(255),
    trade_id                   nvarchar(255),
    created_by                 numeric(19, 0),
    last_modified_by           numeric(19, 0),
    primary key (payment_cancel_response_id)
)
create table payment_certification_request
(
    payment_certification_id numeric(19, 0) identity not null,
    created_date             datetime,
    last_modified_date       datetime,
    edit_date                datetime,
    goods_name               nvarchar(4000),
    merchant_id              nvarchar(255),
    payment_method           nvarchar(255),
    payment_price            int                     not null,
    return_url               nvarchar(255),
    sign_data                nvarchar(255),
    vbank_exp_date           datetime,
    created_by               numeric(19, 0),
    last_modified_by         numeric(19, 0),
    primary key (payment_certification_id)
)
create table payment_certification_response
(
    payment_certification_response_id numeric(19, 0) identity not null,
    created_date                      datetime,
    last_modified_date                datetime,
    auth_token                        nvarchar(255),
    merchant_id                       nvarchar(255),
    net_cancel_url                    nvarchar(4000),
    next_approve_url                  nvarchar(4000),
    pay_method                        nvarchar(255),
    payment_price                     nvarchar(255),
    result_code                       nvarchar(4000),
    result_message                    nvarchar(4000),
    signature                         nvarchar(255),
    trade_id                          nvarchar(255),
    created_by                        numeric(19, 0),
    last_modified_by                  numeric(19, 0),
    primary key (payment_certification_response_id)
)
create table place_order_log
(
    place_order_log_id numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    request_log        nvarchar(4000),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (place_order_log_id)
)
create table point
(
    point_id               numeric(19, 0) identity not null,
    created_date           datetime,
    last_modified_date     datetime,
    amount                 int                     not null,
    misc                   nvarchar(255),
    point_create_date_time datetime,
    point_expired_date     datetime,
    point_kind             nvarchar(255),
    point_state            nvarchar(255),
    created_by             numeric(19, 0),
    last_modified_by       numeric(19, 0),
    member_id              numeric(19, 0),
    order_id               numeric(19, 0),
    order_product_id       numeric(19, 0),
    primary key (point_id)
)
create table point_specific
(
    point_detail_id          numeric(19, 0) identity not null,
    amount                   int                     not null,
    point_kind               nvarchar(255),
    point_state              nvarchar(255),
    remain_point_expire_date datetime,
    point_accum_specific_id  numeric(19, 0),
    point_cancel_specific_id numeric(19, 0),
    member_id                numeric(19, 0),
    order_id                 numeric(19, 0),
    point_id                 numeric(19, 0),
    primary key (point_detail_id)
)
create table product
(
    product_id         numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    discount_amount    int                     not null,
    discount_type      nvarchar(255),
    count_score1       int                     not null,
    count_score2       int                     not null,
    count_score3       int                     not null,
    count_score4       int                     not null,
    count_score5       int                     not null,
    qna_count          int                     not null,
    sales_begin_date   datetime                not null,
    sales_end_date     datetime                not null,
    status             nvarchar(255),
    stock              int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    base_product_id    numeric(19, 0),
    primary key (product_id)
)
create table product_discount_schedules
(
    product_product_id numeric(19, 0) not null,
    begin_date         datetime,
    discount_amount    int            not null,
    discount_type      nvarchar(255),
    end_date           datetime
)
create table product_delivery
(
    product_delivery_id numeric(19, 0) identity not null,
    created_date        datetime,
    last_modified_date  datetime,
    bundle_yn           nvarchar(255),
    delivery_type       nvarchar(255),
    delivery_yn         nvarchar(255),
    fee                 int                     not null,
    fee_condition       int                     not null,
    return_location     nvarchar(255),
    created_by          numeric(19, 0),
    last_modified_by    numeric(19, 0),
    primary key (product_delivery_id)
)
create table provision_notice
(
    provision_notice_id       numeric(19, 0) identity not null,
    created_date              datetime,
    last_modified_date        datetime,
    amount                    int                     not null,
    capacity_unit             nvarchar(4000),
    capacity_unit_text        nvarchar(4000),
    capacity_unit_text_yn     nvarchar(4000),
    unit                      nvarchar(4000),
    certification_association nvarchar(4000),
    certification_code        nvarchar(4000),
    cs_tel                    nvarchar(4000),
    food_type                 nvarchar(4000),
    function_info             nvarchar(4000),
    gmo_yn                    nvarchar(4000),
    import_yn                 nvarchar(4000),
    intake_notice             nvarchar(4000),
    location                  nvarchar(4000),
    manufacturer              nvarchar(4000),
    manufacturing_date        nvarchar(4000),
    no_medicine_guidance      nvarchar(4000),
    nutrition_info            nvarchar(4000),
    provision_type            nvarchar(4000),
    quantity_unit_text        nvarchar(4000),
    quantity_unit_text_yn     nvarchar(4000),
    piece_amount              int                     not null,
    piece_unit                nvarchar(4000),
    quantity                  int                     not null,
    quantity_unit             nvarchar(4000),
    raw_material_contents     nvarchar(4000),
    shelf_life_date           nvarchar(4000),
    created_by                numeric(19, 0),
    last_modified_by          numeric(19, 0),
    primary key (provision_notice_id)
)
create table qna
(
    qna_id               numeric(19, 0) identity not null,
    created_date         datetime,
    last_modified_date   datetime,
    answer               nvarchar(4000),
    answer_content       nvarchar(4000),
    post_date_time       datetime,
    qna_answer_date_time datetime,
    question_content     nvarchar(4000),
    reviewer             nvarchar(4000),
    secret               nvarchar(255),
    status               nvarchar(255),
    created_by           numeric(19, 0),
    last_modified_by     numeric(19, 0),
    member_id            numeric(19, 0),
    product_id           numeric(19, 0),
    primary key (qna_id)
)
create table review
(
    id                 numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    content            nvarchar(4000),
    review_date_time   datetime,
    reviewer           nvarchar(4000),
    score              int                     not null,
    status             nvarchar(255),
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    member_id          numeric(19, 0),
    product_id         numeric(19, 0),
    primary key (id)
)
create table review_images
(
    review_id numeric(19, 0) not null,
    images    nvarchar(255)
)
create table sales_log
(
    sales_log_id            numeric(19, 0) identity not null,
    amount                  int                     not null,
    order_specific_type     nvarchar(255),
    sales_type              nvarchar(255),
    minus_order_specific_id numeric(19, 0),
    order_id                numeric(19, 0),
    plus_order_specific_id  numeric(19, 0),
    primary key (sales_log_id)
)
create table seller
(
    company_name                  nvarchar(4000) not null,
    ceo_name                      nvarchar(4000),
    company_fax                   nvarchar(4000),
    company_location              nvarchar(4000),
    company_type                  nvarchar(4000),
    corporate_registration_number nvarchar(4000),
    cs_center_tel                 nvarchar(4000),
    delivery_condition            int,
    delivery_fee                  int,
    forwarding_address            nvarchar(4000),
    net_sale_report_number        nvarchar(4000),
    return_address                nvarchar(4000),
    return_fee                    int,
    primary key (company_name)
)
create table social_account
(
    social_account_id numeric(19, 0) identity not null,
    social_id         nvarchar(4000),
    social_type       nvarchar(255),
    member_id         numeric(19, 0),
    primary key (social_account_id)
)
create table story
(
    story_id           numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    begin_date_time    datetime,
    content            nvarchar(4000),
    status             int,
    thumbnail_image    nvarchar(255),
    title              nvarchar(255),
    visit_count        int                     not null,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    primary key (story_id)
)
create table wish
(
    wish_id            numeric(19, 0) identity not null,
    created_date       datetime,
    last_modified_date datetime,
    created_by         numeric(19, 0),
    last_modified_by   numeric(19, 0),
    member_id          numeric(19, 0),
    product_id         numeric(19, 0),
    primary key (wish_id)
)
alter table account_group
    add constraint UK_ACCOUNT_GROUP_ALIAS unique (alias)
alter table order_payment
    add constraint UK_ORDER_PAYMENT_PAYMENT_CODE unique (payment_code)
alter table account_group_roles
    add constraint FK_ACCOUNT_GROUP foreign key (account_group_account_group_id) references account_group
alter table base_product
    add constraint FK_BASE_PRODUCT_DELIVERY_INFO foreign key (product_delivery_id) references product_delivery
alter table base_product
    add constraint FK_BASE_PRODUCT_PROVISION_NOTICE foreign key (provision_notice_id) references provision_notice
alter table base_product_product_tags
    add constraint FK_BASE_PRODUCT_TAG foreign key (base_product_base_product_id) references base_product
alter table base_product_thumbnail_image
    add constraint FK_BASE_PRODUCT_THUMBNAIL foreign key (base_product_base_product_id) references base_product
alter table best_review
    add constraint FK_BEST_REVIEW_BEST_REVIEW_PRODUCT foreign key (best_review_product_id) references best_review_product
alter table best_review
    add constraint FK_BEST_REVIEW_REVIEW foreign key (review_id) references review
alter table best_review_product
    add constraint FK_BEST_REVIEW_PRODUCT foreign key (product_id) references product
alter table best_seller
    add constraint FK_BEST_SELLER_PRODUCT foreign key (product_id) references product
alter table cart
    add constraint FK_CART_MEMBER foreign key (member_id) references member
alter table cart
    add constraint FK_CART_PRODUCT foreign key (product_id) references product
alter table category
    add constraint FK_CATEGORY foreign key (parent_category_id) references category
alter table category_product
    add constraint FK_CATEGORY_PRODUCT_PRODUCT foreign key (product_id) references base_product
alter table category_product
    add constraint FK_CATEGORY_PRODUCT_CATEGORY foreign key (category_id) references category
alter table cert
    add constraint FK_CERT_MEMBER foreign key (member_id) references member
alter table coupon_issue_condition
    add constraint FK_COUPON_CONDITION foreign key (coupon_condition_id) references coupon
alter table destination
    add constraint FK_DESTINATION_MEMBER foreign key (member_id) references member
alter table event_contents
    add constraint FK_EVENT_CONTENT foreign key (event_event_id) references event
alter table issue_coupon
    add constraint FK_ISSUE_COUPON_COUPON foreign key (coupon_id) references coupon
alter table issue_coupon
    add constraint FK_ISSUE_COUPON_MEMBER foreign key (member_id) references member
alter table member
    add constraint FK_MEMBER_ACCOUNT_GROUP foreign key (account_group_id) references account_group
alter table member_ban_history
    add constraint FK_MEMBER_BAN_HISTORY_MEMBER foreign key (member_id) references member
alter table member_policy
    add constraint FK_MEMBER_POLICY_MEMBER foreign key (member_id) references member
alter table new_product
    add constraint FK_NEW_PRODUCT_NEW_PRODUCT_CALENDAR foreign key (new_product_calendar_id) references new_product_calendar
alter table new_product
    add constraint FK_NEW_PRODUCT_PRODUCT foreign key (product_id) references product
alter table order_cancel
    add constraint FK_ORDER_CANCEL_ORDER foreign key (order_id) references orders
alter table order_cancel
    add constraint FK_ORDER_CANCEL_ORDER_SPECIFIC foreign key (order_specific_id) references order_specific
alter table order_cancel
    add constraint FK_ORDER_CANCEL_PAYMENT_REQUEST foreign key (payment_cancel_request_id) references payment_cancel_request
alter table order_cancel
    add constraint FK_ORDER_CANCEL_PAYMENT_RESPONSE foreign key (payment_cancel_response_id) references payment_cancel_response
alter table order_exchange_cause_image
    add constraint FK_ORDER_EXCHANGE_CAUSE_IMAGE_ORDER_EXCHANGE foreign key (order_exchange_order_exchange_id) references order_exchange
alter table order_exchange_product
    add constraint FK_ORDER_EXCHANGE_PRODUCT_ORDER_EXCHANGE foreign key (order_exchange_id) references order_exchange
alter table order_payment
    add constraint FK_ORDER_PAYMENT_APPROVE_REQUEST foreign key (payment_approve_request_id) references payment_approve_request
alter table order_payment
    add constraint FK_ORDER_PAYMENT_APPROVE_RESPONSE foreign key (payment_approve_response_id) references payment_approve_response
alter table order_payment
    add constraint FK_ORDER_PAYMENT_CERTIFICATION_REQUEST foreign key (payment_certification_request_id) references payment_certification_request
alter table order_payment
    add constraint FK_ORDER_PAYMENT_CERTIFICATION_RESPONSE foreign key (payment_certification_response_id) references payment_certification_response
alter table order_product
    add constraint FK_ORDER_PRODUCT_ORDER_EXCHANGE_PRODUCT foreign key (order_exchange_product_id) references order_exchange_product
alter table order_product
    add constraint FK_ORDER_PRODUCT_ORDER_SPECIFIC foreign key (order_specific_id) references order_specific
alter table order_product
    add constraint FK_ORDER_PRODUCT_PRODUCT foreign key (product_id) references product
alter table order_product
    add constraint FK_ORDER_PRODUCT_REVIEW foreign key (review_id) references review
alter table order_product_log
    add constraint FK_ORDER_PRODUCT_LOG_ORDER_PRODUCT foreign key (order_product_id) references order_product
alter table orders
    add constraint FK_ORDER_NON_PRODUCT foreign key (non_member_id) references non_member
alter table orders
    add constraint FK_ORDER_MEMBER foreign key (member_id) references member
alter table orders
    add constraint FK_ORDER_ORDER_PAYMENT foreign key (order_payment_id) references order_payment
alter table order_specific
    add constraint FK_ORDER_SPECIFIC_ORDER foreign key (order_id) references orders
alter table order_specific
    add constraint FK_ORDER_SPECIFIC_ORDER_DESTINATION foreign key (order_destination_id) references order_destination
alter table order_specific
    add constraint FK_ORDER_SPECIFIC_ORDER_EXCHANGE foreign key (order_exchange_id) references order_exchange
alter table point
    add constraint FK_POINT_MEMBER foreign key (member_id) references member
alter table point
    add constraint FK_POINT_ORDER foreign key (order_id) references orders
alter table point
    add constraint FK_POINT_ORDER_PRODUCT foreign key (order_product_id) references order_product
alter table point_specific
    add constraint FK_POINT_SPECIFIC_POINT_ACCUM_SPECIFIC foreign key (point_accum_specific_id) references point_specific
alter table point_specific
    add constraint FK_POINT_SPECIFIC_CANCEL_SPECIFIC foreign key (point_cancel_specific_id) references point_specific
alter table point_specific
    add constraint FK_POINT_SPECIFIC_MEMBER foreign key (member_id) references member
alter table point_specific
    add constraint FK_POINT_SPECIFIC_ORDER foreign key (order_id) references orders
alter table point_specific
    add constraint FK_POINT_SPECIFIC_POINT foreign key (point_id) references point
alter table product
    add constraint FK_PRODUCT_BASE_PRODUCT foreign key (base_product_id) references base_product
alter table product_discount_schedules
    add constraint FK_PRODUCT_DISCOUNT_SCHEDULE_PRODUCT foreign key (product_product_id) references product
alter table qna
    add constraint FK_QNA_MEMBER foreign key (member_id) references member
alter table qna
    add constraint FK_QNA_PRODUCT foreign key (product_id) references product
alter table review
    add constraint FK_REVIEW_MEMBER foreign key (member_id) references member
alter table review
    add constraint FK_REVIEW_PRODUCT foreign key (product_id) references product
alter table review_images
    add constraint FK_REVIEW_IMAGE_REVIEW foreign key (review_id) references review
alter table sales_log
    add constraint FK_SALES_LOG_MINUS_ORDER_SPECIFIC foreign key (minus_order_specific_id) references order_specific
alter table sales_log
    add constraint FK_SALES_LOG_MINUS_ORDER foreign key (order_id) references orders
alter table sales_log
    add constraint FK_SALES_LOG_PLUS_ORDER_SPECIFIC foreign key (plus_order_specific_id) references order_specific
alter table social_account
    add constraint FK_SOCIAL_ACCOUNT_MEMBER foreign key (member_id) references member
alter table wish
    add constraint FK_WISH_MEMBER foreign key (member_id) references member
alter table wish
    add constraint FK_WISH_PRODUCT foreign key (product_id) references product
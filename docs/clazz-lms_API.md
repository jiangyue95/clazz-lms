# clazz-lms API Documentation

---

## Common Response Structure

All endpoints return the following wrapper:

| Field | Type    | Description                        |
|-------|---------|------------------------------------|
| code  | integer | `1` = success, `0` = fail          |
| msg   | string  | Message (nullable)                 |
| data  | any     | Response data (nullable on writes) |

---

## Common Enums

**gender**

| Value | Description |
|-------|-------------|
| 1     | Male        |
| 2     | Female      |

**job**

| Value | Description                            |
|-------|----------------------------------------|
| 1     | Head Teacher（班主任）                      |
| 2     | Lecturer（讲师）                           |
| 3     | Student Affairs Supervisor（学管师）        |
| 4     | Teaching and Research Supervisor（教研主管） |
| 5     | Consultant（咨询师）                        |

---

## Employee Management

### 1. List query employees by conditions

**Endpoint:** `GET /emps`

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Query Parameters:**

| Name     | Type    | Required | Description                                 |
|----------|---------|----------|---------------------------------------------|
| name     | string  | No       | Employee name (fuzzy match)                 |
| gender   | integer | No       | Employee gender (see Common Enums)          |
| begin    | string  | No       | Start date of entry date range (yyyy-MM-dd) |
| end      | string  | No       | End date of entry date range (yyyy-MM-dd)   |
| page     | integer | No       | Page number (default: 1)                    |
| pageSize | integer | No       | Items per page (default: 10)                |

Example: `/emps?name=tom&gender=1&begin=2026-01-01&end=2026-12-31&page=1&pageSize=10`

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 2,
    "rows": [
      {
        "id": 1,
        "username": "jinyong",
        "name": "金庸",
        "gender": 1,
        "image": "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg",
        "job": 2,
        "salary": 8000,
        "entryDate": "2015-01-01",
        "deptId": 2,
        "deptName": "教研部",
        "createTime": "2022-09-01T23:06:30",
        "updateTime": "2022-09-02T00:29:04"
      }
    ]
  }
}
```

**`data` object:**

| Field | Type              | Description                        |
|-------|-------------------|------------------------------------|
| total | integer           | Total number of matching employees |
| rows  | array of Employee | Employees on the current page      |

**`Employee` object:**

| Field      | Type    | Description                        |
|------------|---------|------------------------------------|
| id         | integer | Employee ID                        |
| username   | string  | Username                           |
| name       | string  | Display name                       |
| gender     | integer | Gender (see Common Enums)          |
| image      | string  | Avatar URL                         |
| job        | integer | Job type (see Common Enums)        |
| salary     | integer | Salary                             |
| entryDate  | string  | Entry date (yyyy-MM-dd)            |
| deptId     | integer | Department ID                      |
| deptName   | string  | Department name                    |
| createTime | string  | Creation time (ISO 8601)           |
| updateTime | string  | Last update time (ISO 8601)        |

---

### 2. Add employee

**Endpoint:** `POST /emps`

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Request Body (JSON):**

| Field     | Type             | Required | Description                 |
|-----------|------------------|----------|-----------------------------|
| username  | string           | Yes      | Username                    |
| name      | string           | Yes      | Display name                |
| gender    | integer          | Yes      | Gender (see Common Enums)   |
| image     | string           | No       | Avatar URL                  |
| phone     | string           | No       | Phone number                |
| job       | integer          | Yes      | Job type (see Common Enums) |
| salary    | integer          | No       | Salary                      |
| entryDate | string           | Yes      | Entry date (yyyy-MM-dd)     |
| deptId    | integer          | Yes      | Department ID               |
| deptName  | string           | No       | Department name             |
| exprList  | array of EmpExpr | No       | Work experience list        |

**`EmpExpr` object:**

| Field   | Type    | Required | Description              |
|---------|---------|----------|--------------------------|
| begin   | string  | Yes      | Start date (yyyy-MM-dd)  |
| end     | string  | Yes      | End date (yyyy-MM-dd)    |
| company | string  | Yes      | Company name             |
| job     | string  | Yes      | Position at the company  |

Example request body:
```json
{
  "username": "zhangsan",
  "name": "张三",
  "gender": 1,
  "image": "https://example.com/avatar.jpg",
  "phone": "13800000000",
  "job": 2,
  "salary": 8000,
  "entryDate": "2026-01-01",
  "deptId": 2,
  "deptName": "教研部",
  "exprList": [
    {
      "begin": "2020-07-01",
      "end": "2025-12-31",
      "company": "某科技有限公司",
      "job": "Java Developer"
    }
  ]
}
```

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 3. Delete employees

**Endpoint:** `DELETE /emps`

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Query Parameters:**

| Name | Type             | Required | Description                     |
|------|------------------|----------|---------------------------------|
| ids  | array of integer | Yes      | List of employee IDs to delete  |

Example: `DELETE /emps?ids=1,2,3`

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 4. Get employee detail by ID

**Endpoint:** `GET /emps/{id}`

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Path Parameters:**

| Name | Type    | Required | Description |
|------|---------|----------|-------------|
| id   | integer | Yes      | Employee ID |

Example: `GET /emps/1`

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "jinyong",
    "name": "金庸",
    "gender": 1,
    "image": "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg",
    "job": 2,
    "salary": 8000,
    "entryDate": "2015-01-01",
    "status": 1,
    "deptId": 2,
    "createTime": "2022-09-01T23:06:30",
    "updateTime": "2022-09-02T00:29:04",
    "exprList": [
      {
        "id": 1,
        "empId": 1,
        "begin": "2010-01-01",
        "end": "2015-01-01",
        "company": "某公司",
        "job": "Lecturer"
      }
    ]
  }
}
```

**`data` object (`EmpInfoVO`):**

| Field      | Type             | Description                    |
|------------|------------------|--------------------------------|
| id         | integer          | Employee ID                    |
| username   | string           | Username                       |
| name       | string           | Display name                   |
| gender     | integer          | Gender (see Common Enums)      |
| image      | string           | Avatar URL                     |
| job        | integer          | Job type (see Common Enums)    |
| salary     | integer          | Salary                         |
| entryDate  | string           | Entry date (yyyy-MM-dd)        |
| status     | integer          | Employment status              |
| deptId     | integer          | Department ID                  |
| createTime | string           | Creation time (ISO 8601)       |
| updateTime | string           | Last update time (ISO 8601)    |
| exprList   | array of EmpExpr | Work experience list           |

**`EmpExpr` object:**

| Field   | Type    | Description              |
|---------|---------|--------------------------|
| id      | integer | Experience record ID     |
| empId   | integer | Employee ID              |
| begin   | string  | Start date (yyyy-MM-dd)  |
| end     | string  | End date (yyyy-MM-dd)    |
| company | string  | Company name             |
| job     | string  | Position at the company  |

---

### 5. Update employee

**Endpoint:** `PUT /emps`

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Request Body (JSON):**

| Field     | Type             | Required | Description                         |
|-----------|------------------|----------|-------------------------------------|
| id        | integer          | Yes      | Employee ID                         |
| username  | string           | No       | Username                            |
| name      | string           | No       | Display name                        |
| gender    | integer          | No       | Gender (see Common Enums)           |
| image     | string           | No       | Avatar URL                          |
| phone     | string           | No       | Phone number                        |
| job       | integer          | No       | Job type (see Common Enums)         |
| salary    | integer          | No       | Salary                              |
| entryDate | string           | No       | Entry date (yyyy-MM-dd)             |
| deptId    | integer          | No       | Department ID                       |
| exprList  | array of EmpExpr | No       | Work experience list (full replace) |

Example request body:
```json
{
  "id": 1,
  "name": "金庸",
  "salary": 9000,
  "exprList": [
    {
      "id": 1,
      "empId": 1,
      "begin": "2010-01-01",
      "end": "2015-01-01",
      "company": "某公司",
      "job": "Lecturer"
    }
  ]
}
```

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

---

### 6. Get all employees (basic info)

**Endpoint:** `GET /emps/list`

**Description:** Returns a flat list of all employees without pagination. Typically used to populate dropdown selectors.

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**No query parameters.**

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "username": "jinyong",
      "name": "金庸",
      "gender": 1,
      "image": "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg",
      "job": 2,
      "salary": 8000,
      "entryDate": "2015-01-01",
      "deptId": 2,
      "deptName": "教研部",
      "createTime": "2022-09-01T23:06:30",
      "updateTime": "2022-09-02T00:29:04"
    }
  ]
}
```

`data` is an array of `Employee` objects. See the field descriptions in [Section 1](#1-list-query-employees-by-conditions).

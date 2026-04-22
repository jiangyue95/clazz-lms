# clazz-lms Statistics API Documentation

---

## Common Response Structure

All endpoints return the following wrapper:

| Field | Type    | Description               |
|-------|---------|---------------------------|
| code  | integer | `1` = success, `0` = fail |
| msg   | string  | Message (nullable)        |
| data  | any     | Response data             |

---

## Statistics / Report

Base path: `/report`

---

### 1. Get Employee Job Distribution

**Endpoint:** `GET /report/empJobData`

**Description:** Returns the count of employees grouped by job position. Intended for bar/pie chart rendering — `jobList` and `dataList` are parallel arrays of the same length.

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
  "data": {
    "jobList": ["Head Teacher", "Lecturer", "Student Affairs Supervisor", "Teaching and Research Supervisor", "Consultant"],
    "dataList": [3, 10, 5, 2, 4]
  }
}
```

**`data` object (`EmpJobOptionVO`):**

| Field    | Type             | Description                                               |
|----------|------------------|-----------------------------------------------------------|
| jobList  | array of string  | Job position labels (parallel to `dataList`)              |
| dataList | array of integer | Employee count for each position (parallel to `jobList`)  |

---

### 2. Get Employee Gender Distribution

**Endpoint:** `GET /report/empGenderData`

**Description:** Returns the count of employees grouped by gender. Intended for pie chart rendering.

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
    { "name": "Male", "value": 15 },
    { "name": "Female", "value": 9 }
  ]
}
```

**`data` is an array of `EmpGenderVO` objects:**

| Field | Type    | Description                     |
|-------|---------|---------------------------------|
| name  | string  | Gender label (`"Male"` / `"Female"`)   |
| value | integer | Employee count for that gender  |

---

### 3. Get Student Count per Class

**Endpoint:** `GET /report/studentCountData`

**Description:** Returns the number of students in each class. Intended for bar chart rendering — `clazzList` and `dataList` are parallel arrays of the same length.

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
  "data": {
    "clazzList": ["Java Employment Class", "Frontend Employment Class", "Python Employment Class"],
    "dataList": [35, 28, 20]
  }
}
```

**`data` object (`ClazzCountVO`):**

| Field     | Type             | Description                                              |
|-----------|------------------|----------------------------------------------------------|
| clazzList | array of string  | Class name labels (parallel to `dataList`)               |
| dataList  | array of integer | Student count for each class (parallel to `clazzList`)   |

---

### 4. Get Student Degree Distribution

**Endpoint:** `GET /report/studentDegreeData`

**Description:** Returns the count of students grouped by academic degree. Intended for pie chart rendering.

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
    { "name": "Junior College", "value": 20 },
    { "name": "Bachelor", "value": 45 },
    { "name": "Master", "value": 10 },
    { "name": "PhD", "value": 2 }
  ]
}
```

**`data` is an array of `StudentDegreeVO` objects:**

| Field | Type    | Description                      |
|-------|---------|----------------------------------|
| name  | string  | Degree label (e.g. `"Bachelor"`)  |
| value | integer | Student count for that degree    |

---

## Operation Log

Base path: `/log`

---

### 5. Get Operation Logs (Paginated)

**Endpoint:** `GET /log/page`

**Description:** Returns a paginated list of employee operation logs, ordered by operation time descending.

**Request Headers:**

| Name  | Type   | Required | Description  |
|-------|--------|----------|--------------|
| token | string | Yes      | Bearer token |

**Query Parameters:**

| Name     | Type    | Required | Description                  |
|----------|---------|----------|------------------------------|
| page     | integer | Yes      | Page number (starts from 1)  |
| pageSize | integer | Yes      | Items per page               |

Example: `GET /log/page?page=1&pageSize=10`

**Success Response (200 OK):**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 100,
    "rows": [
      {
        "id": 1,
        "operateEmpId": 2,
        "operateEmpName": "金庸",
        "operateTime": "2026-04-21T10:30:00",
        "className": "com.yue.controller.EmpController",
        "methodName": "save",
        "methodParams": "[{\"name\":\"张三\",\"gender\":1}]",
        "returnValue": "{\"code\":1,\"msg\":\"success\",\"data\":null}",
        "costTime": 38
      }
    ]
  }
}
```

**`data` object (`PageResult<OperateLog>`):**

| Field | Type                  | Description                      |
|-------|-----------------------|----------------------------------|
| total | integer               | Total number of log records      |
| rows  | array of OperateLog   | Log records on the current page  |

**`OperateLog` object:**

| Field          | Type    | Description                                      |
|----------------|---------|--------------------------------------------------|
| id             | integer | Log record ID                                    |
| operateEmpId   | integer | ID of the employee who performed the operation   |
| operateEmpName | string  | Name of the employee who performed the operation |
| operateTime    | string  | Time of the operation (ISO 8601)                 |
| className      | string  | Fully-qualified class name of the called method  |
| methodName     | string  | Name of the called method                        |
| methodParams   | string  | JSON-serialized method parameters                |
| returnValue    | string  | JSON-serialized return value                     |
| costTime       | integer | Method execution time in milliseconds            |

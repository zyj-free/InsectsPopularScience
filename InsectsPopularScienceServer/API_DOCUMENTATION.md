# 昆虫科普后端API文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **认证方式**: JWT Bearer Token
- **请求头**: `Authorization: Bearer {token}`

## 响应格式

所有API响应统一使用以下格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

## API接口列表

### 1. 用户认证

#### 1.1 用户注册
- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Auth**: 不需要
- **Request Body**:
```json
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000",
  "nickname": "测试用户"
}
```
- **Response**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": 1,
    "username": "testuser",
    "nickname": "测试用户"
  }
}
```

#### 1.2 用户登录
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Auth**: 不需要
- **Request Body**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```
- **Response**: 同注册接口

---

### 2. 昆虫相关

#### 2.1 获取昆虫列表
- **URL**: `/api/insects/list`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）
  - `categoryId`: 分类ID（可选）
- **Response**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [...],
    "totalElements": 100,
    "totalPages": 10,
    "number": 0,
    "size": 10
  }
}
```

#### 2.2 获取昆虫详情
- **URL**: `/api/insects/{id}`
- **Method**: `GET`
- **Auth**: 不需要
- **Response**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "蝴蝶",
    "scientificName": "Lepidoptera",
    "categoryId": 1,
    "description": "...",
    "morphologicalFeatures": "...",
    "livingHabits": "...",
    "habitat": "...",
    "distribution": "...",
    "imageUrls": "[\"url1\", \"url2\"]",
    "viewCount": 100
  }
}
```

#### 2.3 搜索昆虫
- **URL**: `/api/insects/search`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `keyword`: 搜索关键词
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

#### 2.4 获取热门昆虫
- **URL**: `/api/insects/popular`
- **Method**: `GET`
- **Auth**: 不需要

---

### 3. 分类相关

#### 3.1 获取所有分类
- **URL**: `/api/categories/list`
- **Method**: `GET`
- **Auth**: 不需要

#### 3.2 根据父分类获取子分类
- **URL**: `/api/categories/parent/{parentId}`
- **Method**: `GET`
- **Auth**: 不需要

#### 3.3 根据级别获取分类
- **URL**: `/api/categories/level/{level}`
- **Method**: `GET`
- **Auth**: 不需要
- **说明**: level: 1=目, 2=科, 3=属

---

### 4. 科普文章

#### 4.1 获取文章列表
- **URL**: `/api/articles/list`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

#### 4.2 获取文章详情
- **URL**: `/api/articles/{id}`
- **Method**: `GET`
- **Auth**: 不需要

#### 4.3 获取推荐文章
- **URL**: `/api/articles/recommend`
- **Method**: `GET`
- **Auth**: 不需要

#### 4.4 搜索文章
- **URL**: `/api/articles/search`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `keyword`: 搜索关键词
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

---

### 5. 收藏功能（需要登录）

#### 5.1 收藏/取消收藏
- **URL**: `/api/favorites/toggle/{insectId}`
- **Method**: `POST`
- **Auth**: 需要
- **Response**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "isFavorite": true,
    "message": "收藏成功"
  }
}
```

#### 5.2 获取我的收藏列表
- **URL**: `/api/favorites/list`
- **Method**: `GET`
- **Auth**: 需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

#### 5.3 检查是否已收藏
- **URL**: `/api/favorites/check/{insectId}`
- **Method**: `GET`
- **Auth**: 需要

---

### 6. 学习笔记（需要登录）

#### 6.1 创建笔记
- **URL**: `/api/notes/create`
- **Method**: `POST`
- **Auth**: 需要
- **Request Body**:
```json
{
  "insectId": 1,
  "content": "笔记内容"
}
```

#### 6.2 更新笔记
- **URL**: `/api/notes/{noteId}`
- **Method**: `PUT`
- **Auth**: 需要
- **Request Body**:
```json
{
  "content": "更新后的笔记内容"
}
```

#### 6.3 删除笔记
- **URL**: `/api/notes/{noteId}`
- **Method**: `DELETE`
- **Auth**: 需要

#### 6.4 获取我的笔记列表
- **URL**: `/api/notes/list`
- **Method**: `GET`
- **Auth**: 需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）
  - `insectId`: 昆虫ID（可选，筛选特定昆虫的笔记）

#### 6.5 获取笔记详情
- **URL**: `/api/notes/{noteId}`
- **Method**: `GET`
- **Auth**: 需要

---

### 7. 社区分享

#### 7.1 创建分享
- **URL**: `/api/shares/create`
- **Method**: `POST`
- **Auth**: 需要
- **Request Body**:
```json
{
  "insectId": 1,
  "imageUrl": "https://example.com/image.jpg",
  "description": "分享描述"
}
```

#### 7.2 获取分享列表
- **URL**: `/api/shares/list`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

#### 7.3 获取我的分享
- **URL**: `/api/shares/my`
- **Method**: `GET`
- **Auth**: 需要

#### 7.4 获取分享详情
- **URL**: `/api/shares/{id}`
- **Method**: `GET`
- **Auth**: 不需要

#### 7.5 删除分享
- **URL**: `/api/shares/{id}`
- **Method**: `DELETE`
- **Auth**: 需要

---

### 8. 评论功能

#### 8.1 创建评论
- **URL**: `/api/comments/create`
- **Method**: `POST`
- **Auth**: 需要
- **Request Body**:
```json
{
  "shareId": 1,
  "content": "评论内容",
  "parentId": null
}
```
- **说明**: `parentId`为null表示顶级评论，否则为回复评论

#### 8.2 获取分享的评论列表
- **URL**: `/api/comments/share/{shareId}`
- **Method**: `GET`
- **Auth**: 不需要
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页数量（默认10）

#### 8.3 获取评论树（嵌套结构）
- **URL**: `/api/comments/tree/{shareId}`
- **Method**: `GET`
- **Auth**: 不需要

#### 8.4 删除评论
- **URL**: `/api/comments/{commentId}`
- **Method**: `DELETE`
- **Auth**: 需要

#### 8.5 获取评论详情
- **URL**: `/api/comments/{commentId}`
- **Method**: `GET`
- **Auth**: 不需要

---

## 错误码说明

- `200`: 操作成功
- `400`: 请求参数错误或业务逻辑错误
- `500`: 服务器内部错误

## 数据库配置

在 `application.properties` 中配置数据库连接：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/insects_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=root
```

## 启动说明

1. 确保MySQL数据库已安装并运行
2. 创建数据库：`CREATE DATABASE insects_db;`
3. 修改 `application.properties` 中的数据库配置
4. 运行 `InsectsPopularScienceApplication.java`
5. 应用将在 `http://localhost:8080` 启动

数据库表将自动创建（JPA自动建表）。


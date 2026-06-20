# 昆虫科普 Android 应用

基于HarmonyOS项目开发的Android版本，使用Java语言开发，UI美观现代。

## 功能特性

- ✅ 用户注册/登录
- ✅ 昆虫列表浏览（支持搜索、分页）
- ✅ 昆虫详情查看（支持收藏）
- ✅ 科普文章浏览
- ✅ 社区分享（发布、查看、评论）
- ✅ 我的收藏
- ✅ 我的分享
- ✅ 个人中心（修改资料、修改密码）

## 技术栈

- **开发语言**: Java
- **UI框架**: Material Design Components
- **网络请求**: Retrofit + OkHttp
- **图片加载**: Glide
- **架构**: MVC模式
- **最低支持**: Android 7.0 (API 24)

## 项目结构

```
app/src/main/java/com/example/insectspopularscience/
├── activity/          # Activity类
│   ├── SplashActivity.java
│   ├── LoginActivity.java
│   ├── MainActivity.java
│   ├── InsectDetailActivity.java
│   ├── PublishShareActivity.java
│   ├── ShareDetailActivity.java
│   ├── MyFavoritesActivity.java
│   ├── MySharesActivity.java
│   ├── UpdateProfileActivity.java
│   └── ChangePasswordActivity.java
├── fragment/          # Fragment类
│   ├── HomeFragment.java
│   ├── ArticleFragment.java
│   ├── CommunityFragment.java
│   └── ProfileFragment.java
├── adapter/           # RecyclerView适配器
│   ├── InsectAdapter.java
│   ├── ArticleAdapter.java
│   ├── ShareAdapter.java
│   ├── CommentAdapter.java
│   └── BannerAdapter.java
├── model/             # 数据模型
│   ├── User.java
│   ├── Insect.java
│   ├── Article.java
│   ├── Share.java
│   ├── Comment.java
│   └── ...
├── api/               # API接口
│   ├── ApiService.java
│   └── RetrofitClient.java
└── util/              # 工具类
    ├── ImageUtil.java
    ├── DateUtil.java
    └── StorageUtil.java
```

## 配置说明

### 1. 修改后端API地址

编辑 `app/src/main/java/com/example/insectspopularscience/api/RetrofitClient.java`，修改BASE_URL：

```java
private static final String BASE_URL = "http://你的服务器IP:8080";
```

**注意**：
- 如果使用模拟器，IP地址通常是 `10.0.2.2`
- 如果使用真机，需要确保手机和电脑在同一网络，使用电脑的局域网IP
- 确保后端服务已启动并可访问

### 2. 网络权限

已在AndroidManifest.xml中配置：
- `INTERNET` 权限
- `ACCESS_NETWORK_STATE` 权限

## 依赖库

项目使用的主要依赖库：

- `com.squareup.retrofit2:retrofit:2.9.0` - 网络请求
- `com.squareup.retrofit2:converter-gson:2.9.0` - JSON转换
- `com.squareup.okhttp3:okhttp:4.12.0` - HTTP客户端
- `com.github.bumptech.glide:glide:4.16.0` - 图片加载
- `de.hdodenhof:circleimageview:3.1.0` - 圆形头像
- `com.google.android.material:material` - Material Design组件

## UI设计

- 采用Material Design设计规范
- 主色调：绿色 (#4CAF50)
- 卡片式布局，圆角设计
- 支持下拉刷新和上拉加载更多
- 响应式布局，适配不同屏幕尺寸

## 主要功能说明

### 启动页
- 显示2秒后根据登录状态跳转

### 登录/注册
- 支持用户名密码登录
- 支持邮箱注册
- 自动保存Token

### 主页（昆虫）
- 顶部轮播图
- 搜索功能
- 分页加载昆虫列表
- 下拉刷新

### 昆虫详情
- 图片轮播
- 详细信息展示
- 收藏功能

### 社区分享
- 发布分享（标题、图片、描述）
- 查看分享列表
- 查看分享详情
- 评论功能

### 个人中心
- 查看个人信息
- 我的收藏
- 我的分享
- 修改资料
- 修改密码

## 编译运行

1. 使用Android Studio打开项目
2. 同步Gradle依赖
3. 配置后端API地址
4. 连接设备或启动模拟器
5. 运行项目

## 注意事项

1. 确保后端服务已启动
2. 确保网络连接正常
3. 首次运行需要注册账号
4. 部分功能需要登录后才能使用

## 后续优化建议

- [ ] 添加图片选择器，支持本地图片上传
- [ ] 添加图片缓存机制
- [ ] 优化列表加载性能
- [ ] 添加离线缓存功能
- [ ] 添加推送通知
- [ ] 优化UI动画效果


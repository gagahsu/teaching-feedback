# 每日教學回饋系統

Spring Boot · Angular · PostgreSQL (Supabase) · Deployed on Render

## 架構

```
frontend/   Angular 17 (Render Static Site)
backend/    Spring Boot 3 (Render Web Service)
database/   schema.sql → Supabase PostgreSQL
```

## 本地開發

### 前置條件
- Java 17+
- Node 18+
- PostgreSQL (或直接用 Supabase)

### Backend

```bash
cd backend
# 複製環境變數
cp .env.example .env   # 填入 DATABASE_URL, JWT_SECRET
mvn spring-boot:run
```

環境變數：
| 變數 | 說明 |
|---|---|
| `DATABASE_URL` | `jdbc:postgresql://host:5432/postgres?user=xxx&password=xxx` |
| `JWT_SECRET` | 至少 32 字元的隨機字串 |

### Frontend

```bash
cd frontend
npm install
# 開發時打 backend: http://localhost:8080
ng serve
```

生產環境：修改 `src/environments/environment.prod.ts` 中的 `apiUrl` 為 Render backend URL。

## Deploy (Render)

1. Fork/push 此 repo 到 GitHub
2. Render Dashboard → **New Web Service** → 選此 repo → Docker → `./backend/Dockerfile`
   - 加環境變數 `DATABASE_URL`（Supabase 連線字串，Session mode，port 5432）
   - `JWT_SECRET` 自動產生或手填
3. 記下 backend URL（如 `https://tfb-backend.onrender.com`）
4. 修改 `frontend/src/environments/environment.prod.ts` → `apiUrl` 填入上方 URL + `/api`
5. Render Dashboard → **New Static Site** → 選此 repo
   - Build command: `cd frontend && npm ci && npm run build`
   - Publish directory: `frontend/dist/frontend/browser`

## Supabase 資料庫初始化

1. 建立 Supabase 專案
2. SQL Editor → 貼上 `database/schema.sql` 執行
3. Settings → Database → Connection String (Session mode) → 複製給 Render

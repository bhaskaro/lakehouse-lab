# 📘 Lakehouse Lab – Spark + Iceberg REST + MinIO + Java Client

This repository contains a **fully containerized Iceberg Lakehouse architecture** including:

* 🔥 Apache Spark (Iceberg-enabled)
* 🌐 Iceberg REST Catalog
* 🪣 MinIO (S3-compatible object storage)
* ☕ Java REST client for catalog automation
* 🐳 Docker-based reproducible environment

This lab demonstrates **real-world Iceberg architecture patterns** used in production lakehouse systems.

---

# 🏗 Architecture Overview

```
                +-------------------+
                |   Java REST Client|
                +---------+---------+
                          |
                          v
                +-------------------+
                |  Iceberg REST     |
                |  Catalog Server   |
                +---------+---------+
                          |
                          v
                +-------------------+
                |   MinIO (S3)      |
                |   warehouse bucket|
                +-------------------+

                +-------------------+
                | Spark (Optional)  |
                | Query Engine      |
                +-------------------+
```

### Responsibilities

| Component | Role                                   |
| --------- | -------------------------------------- |
| REST      | Metadata control plane                 |
| MinIO     | Object storage (data + metadata files) |
| Spark     | Compute engine                         |
| Java      | Catalog automation                     |

---

# 📂 Repository Structure

```
lakehouse-lab/
│
├── iceberg-rest/
│   ├── docker-compose.yml
│   ├── warehouse/
│   └── iceberg-rest-client/
│       ├── pom.xml
│       └── src/
│
└── spark-cluster/
    └── single-cluster-multi-worker/
        └── docker-compose.yml
```

---

# 🚀 Getting Started

## 1️⃣ Clone Repository

```bash
git clone https://github.com/bhaskaro/lakehouse-lab.git
cd lakehouse-lab
```

---

# 🐳 Start Iceberg REST + MinIO

Navigate to:

```bash
cd iceberg-rest
docker compose up -d
```

Verify containers:

```bash
docker compose ps
```

You should see:

* iceberg-rest
* minio
* spark-iceberg (if included)
* mc

---

# 🌐 Verify REST Server

```bash
curl http://localhost:8181/v1/config
```

Expected:

```json
{"defaults":{},"overrides":{}}
```

---

# 🪣 MinIO Console

Open:

```
http://localhost:9001
```

Login:

```
Username: admin
Password: password
```

You should see bucket:

```
warehouse
```

---

# ☕ Build & Run Java REST Client

Navigate to Java client:

```bash
cd iceberg-rest/iceberg-rest-client
```

## Build

```bash
mvn clean install
```

## Run

```bash
mvn exec:java -Dexec.mainClass="com.lakehouse.Main"
```

---

# 🔄 What Java Client Does

The client:

* Checks if namespace exists
* Drops namespace (if present)
* Creates namespace
* Creates table
* Lists namespaces
* Lists tables

It uses Iceberg REST endpoints:

```
POST /v1/namespaces
POST /v1/namespaces/{ns}/tables
DELETE /v1/namespaces/{ns}
DELETE /v1/namespaces/{ns}/tables/{table}
```

---

# 📦 Verify Objects in MinIO

After running the client:

Open MinIO → warehouse bucket.

You should see:

```
warehouse/
  prod_ns/
    prod_table/
      metadata/
```

---

# 🧪 Using Spark with REST Catalog

If Spark container is running:

```bash
docker exec -it spark-iceberg bash
spark-sql
```

Inside Spark:

```sql
SHOW CATALOGS;
```

You should see:

```
demo
spark_catalog
```

List namespaces:

```sql
SHOW NAMESPACES IN demo;
```

Query table:

```sql
SELECT * FROM demo.prod_ns.prod_table;
```

---

# 🔍 REST API Examples

List namespaces:

```bash
curl http://localhost:8181/v1/namespaces
```

Create namespace:

```bash
curl -X POST http://localhost:8181/v1/namespaces \
  -H "Content-Type: application/json" \
  -d '{"namespace":["demo_ns"]}'
```

List tables:

```bash
curl http://localhost:8181/v1/namespaces/demo_ns/tables
```

---

# 🧠 Key Learning Concepts

This lab demonstrates:

* REST-based Iceberg catalogs
* Object storage separation from compute
* Metadata control plane vs data plane
* Multi-engine interoperability
* Idempotent catalog automation

---

# 🛑 Stop Environment

```bash
docker compose down -v
```

---

# 🧩 Future Extensions

* Add snapshot inspection via REST
* Add multi-catalog dev/prod
* Add Spark data insert jobs
* Add Iceberg Java write API
* Add Kubernetes deployment
* Add CI/CD for namespace provisioning

---

# 🎯 Summary

If you understand this lab, you understand:

* How Iceberg REST works
* How Spark connects to REST catalogs
* How metadata is stored
* How S3-compatible storage integrates
* How production lakehouses separate concerns

This is enterprise-grade Iceberg architecture.

---

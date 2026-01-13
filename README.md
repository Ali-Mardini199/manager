# مشروع إدارة المواعيد لمادة برمجة التطبيقات الشبكية

## شرح المشروع
يقوم هذا المشروع بإدارة المواعيد الخاصة بالخدمات المقدّمة من قبل مقدّمي الخدمات، حيث يتيح:
- عرض المواعيد المحجوزة ومتابعتها.
- تمكين الزبائن من حجز موعد لأي خدمة بغض النظر عن مقدمها.
- التأكد من أن الخدمة متوفرة وأن الموعد المراد حجزه غير محجوز مسبقًا.

يهدف النظام إلى تسهيل عملية إدارة وحجز المواعيد بطريقة منظمة وفعّالة.

---

## تقسيم العمل
- **الطالب حاتم جادالله العوابده** و **الطالب مصطفى حسين القاسم**: تنفيذ طلبات المرحلة الأولى، وأول طلب من المرحلة الثانية.
- **الطالب علي ماهر مارديني**: تنفيذ الطلب الثاني والثالث من المرحلة الثانية، بالإضافة إلى طلبات المرحلة الثالثة.

---

## التقنيات المستخدمة
- Java
- Spring Boot
- Spring Data JPA
- Spring Web
- قاعدة بيانات **H2 (In-Memory Database)**
- Maven

---

## كيفية تشغيل المشروع (باستخدام H2 Database)

### المتطلبات المسبقة
- تثبيت **Java JDK 17** (أو الإصدار المطلوب في المشروع)
- تثبيت **Maven**
- أي بيئة تطوير تدعم Java مثل:
  - IntelliJ IDEA
  - Eclipse
  - VS Code

---

### إعداد قاعدة بيانات H2
المشروع يستخدم قاعدة بيانات **H2** داخل الذاكرة (In-Memory)، ولا يحتاج إلى أي إعداد خارجي.

تأكد من وجود الإعدادات التالية (أو ما يشابهها) في ملف `application.properties` أو `application.yml`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

### تشغيل المشروع

#### الطريقة الأولى: من خلال بيئة التطوير (IDE)
1. افتح المشروع باستخدام بيئة تطوير Java.
2. انتقل إلى الكلاس الرئيسي الذي يحتوي على:
   ```java
   @SpringBootApplication
   ```
3. شغّل المشروع باستخدام زر **Run**.

---

#### الطريقة الثانية: باستخدام Maven
من داخل مجلد المشروع، نفّذ الأمر التالي:

```bash
mvn spring-boot:run
```

أو:

```bash
mvn clean install
java -jar target/project-name.jar
```

(استبدل `project-name.jar` باسم ملف الـ jar الناتج).

---

### الوصول إلى H2 Console
بعد تشغيل المشروع، يمكن الدخول إلى واجهة H2 من خلال المتصفح:

```
http://localhost:8080/h2-console
```

بيانات الاتصال:
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (فارغ)

---

## توثيق واجهات برمجة التطبيقات (API Documentation)

### Swagger UI
يوفر المشروع توثيقًا تفاعليًا لواجهات برمجة التطبيقات باستخدام **Swagger**، ويمكن الوصول إليه بعد تشغيل التطبيق عبر الرابط التالي:

```
http://localhost:8080/swagger-ui/index.html
```

من خلال Swagger يمكنك:
- استعراض جميع الـ APIs المتاحة
- تجربة الطلبات مباشرة من المتصفح
- الاطلاع على نماذج الطلبات والاستجابات

---

## الخدمات (Services)
يحتوي المشروع على مجموعة من الخدمات (Services)، وكل خدمة تمثل مجموعة من واجهات برمجة التطبيقات المرتبطة بها:

- **User Service**
- **Service Management Service**
- **Appointment Service**
- **Admin Service**
- **Authentication Service (Login & Sign Up)**
- **Role Service**
- **Staff Service**

---

## ملاحظات
- قاعدة بيانات H2 مؤقتة، ويتم حذف البيانات عند إيقاف التطبيق.
- مناسبة للتجربة، التطوير، والاختبارات فقط.
- في حال الرغبة باستخدام قاعدة بيانات فعلية (MySQL أو PostgreSQL)، يمكن تعديل إعدادات الاتصال بسهولة.



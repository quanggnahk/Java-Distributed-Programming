// 1. Xóa dữ liệu cũ 
MATCH (n) DETACH DELETE n;

// 2. Tạo các nút Person mẫu
CREATE (p1:Person {name: "Nguyen Van A", age: 25, birthday: date("2001-05-12"), createdAt: localdatetime("2026-01-10T08:30:00")})
CREATE (p2:Person {name: "Tran Thi B", age: 22, birthday: date("2004-09-20"), createdAt: localdatetime("2026-02-15T09:15:00")})
CREATE (p3:Person {name: "Le Van C", age: 28, birthday: date("1998-11-05"), createdAt: localdatetime("2026-03-01T14:20:00")})
CREATE (p4:Person {name: "Pham Minh D", age: 30, birthday: date("1996-03-30"), createdAt: localdatetime("2026-04-12T11:00:00")})
CREATE (p5:Person {name: "Hoang Thi E", age: 24, birthday: date("2002-07-18"), createdAt: localdatetime("2026-05-25T16:45:00")});

// 3. Tạo các mối quan hệ FRIEND (Bạn bè)
MATCH (a:Person {name: "Nguyen Van A"}), (b:Person {name: "Tran Thi B"}) CREATE (a)-[:FRIEND]->(b);
MATCH (a:Person {name: "Nguyen Van A"}), (c:Person {name: "Le Van C"}) CREATE (a)-[:FRIEND]->(c);
MATCH (a:Person {name: "Nguyen Van A"}), (d:Person {name: "Pham Minh D"}) CREATE (a)-[:FRIEND]->(d);

MATCH (b:Person {name: "Tran Thi B"}), (c:Person {name: "Le Van C"}) CREATE (b)-[:FRIEND]->(c);
MATCH (b:Person {name: "Tran Thi B"}), (e:Person {name: "Hoang Thi E"}) CREATE (b)-[:FRIEND]->(e);

MATCH (c:Person {name: "Le Van C"}), (d:Person {name: "Pham Minh D"}) CREATE (c)-[:FRIEND]->(d);

MATCH (e:Person {name: "Hoang Thi E"}), (a:Person {name: "Nguyen Van A"}) CREATE (e)-[:FRIEND]->(a);
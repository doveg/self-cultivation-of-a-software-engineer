## OSI

开放式系统互联模型 / Open System Interconnection Model

---

#### 基础部分

###### OSI 7 层模型-asc

记忆口诀： Please Do Not Take Sales People's Advice

1. 物理层（Physical Layer）
    - 在局域网上发送数据帧（Data Frame），它负责管理电脑通信设备和网络媒体之间的互通
    - 包括了针脚、电压、线缆规范、集线器、中继器、网卡、主机接口卡等
    - 例如：调制解调器、无线电、光纤

2. 数据链路层（Data Link Layer）
    - 负责网络寻址、错误侦测和改错
    - 当表头和表尾被加至数据包时，会形成信息框（DataFrame）。
    - 数据链表头（DLH）是包含了物理地址和错误侦测及改错的方法。
    - 数据链表尾（DLT）是一串指示数据包末端的字符串。
    - 例如以太网、Wi-Fi（无线局域网）、GPRS（通用分组无线服务）、令牌环、HDLC、帧中继、ISDN、ATM、IEEE 802.11、FDDI、PPP

    - 分为两个子层：
        - 逻辑链路控制（logical link control，LLC）子层
        - 介质访问控制（Media access control，MAC）子层

3. 网络层（Network Layer）
    - 决定数据的路径选择和转寄，将网络表头（NH）加至数据包，以形成分组
    - 网络表头包含了网络资料
    - 例如：IP（互联网协议）、ICMP、IPX、BGP、OSPF、RIP、IGRP、EIGRP、ARP、RARP、X.25

4. 传输层（Transport Layer）
    - 把传输表头（TH）加至资料以形成分组
    - 传输表头包含了所使用的协议等发送信息
    - 例如：TCP（传输控制协议）、UDP、RTP、SCTP、SPX、ATP、IL

5. 会话层（Session Layer）
    - 负责在数据传输中设置和维护计算机网络中两台计算机之间的通信连接
    - 例如：ASAP、ISO 8327 / CCITT X.225、RPC、NetBIOS、Winsock、BSD sockets、SOCKS、PAP

6. 表示层（Presentation Layer）
    - 把数据转换为能与接收者的系统格式兼容并适合传输的格式
    - 例如：XDR、ASN.1、NCP、TLS、ASCII

7. 应用层（Application Layer）
    - 提供为应用软件而设计的接口，以设置与另一应用软件之间的通信
    - 例如：HTTP、HTTPS、SSH、FTP、Telnet、SMTP、SNMP、POP3、SIP、SSH、NFS、RTSP、XMPP、Whois、ENRP、TLS

**P**lease **D**o **N**ot **T**ake **S**ales **P**eople's **A**dvice

###### OSI 7 层模型-dec

记忆口诀： All People Seem To Need Data Processing

第 7 层：应用层（Application Layer）

提供为应用软件而设计的接口，以设置与另一应用软件之间的通信

第 6 层：表示层（Presentation Layer）

把数据转换为能与接收者的系统格式兼容并适合传输的格式

第 5 层：会话层（Session Layer）

负责在数据传输中设置和维护计算机网络中两台计算机之间的通信连接

第 4 层：传输层（Transport Layer）

把传输表头（TH）加至资料以形成分组。传输表头包含了所使用的协议等发送信息

第 3 层：网络层（Network Layer）

决定数据的路径选择和转寄，将网络表头（NH）加至数据包，以形成分组。网络表头包含了网络资料

第 2 层：数据链路层（Data Link Layer）负责网络寻址、错误侦测和改错。

- 当表头和表尾被加至数据包时，会形成信息框（DataFrame）。
- 数据链表头（DLH）是包含了物理地址和错误侦测及改错的方法。
- 数据链表尾（DLT）是一串指示数据包末端的字符串

分为两个子层：

1. 逻辑链路控制（logical link control，LLC）子层
2. 介质访问控制（Media access control，MAC）子层

第 1 层：物理层（Physical Layer）

在局域网上发送数据帧（Data Frame），它负责管理电脑通信设备和网络媒体之间的互通

######                                                                                       

---

#### 提高部分

######

---










---

参考链接：

- [OSI 模型](https://zh.wikipedia.org/wiki/OSI%E6%A8%A1%E5%9E%8B)

---














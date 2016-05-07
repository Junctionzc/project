# Flask入门学习笔记
**配合《Flask Web开发：基于Python的Web应用开发实战》学习**

## **chapter 1**
### 虚拟环境virtualenv
1. 安装virtualenv包：`$ sudo apt-get install python-virtualenv`
2. 创建虚拟环境：`$ virtualenv venv`
3. 激活虚拟环境：`$ source venv/bin/activate`
4. 退出虚拟环境：`$ deactivate`

### Flask
1. 安装Flask：`(venv) $ pip install flask`
2. 检查Flask是否安装正确：
```
(venv) $ python
>> import flask
>> 
```
没有看见错误证明Flask安装正确

## **chapter 2**
### Jinja2模板引擎
>为了分离业务逻辑和表现逻辑，一般使用模板，模板是一个包含响应文本的文件，用占位符表示动态部分，渲染就是用真实值替换变量，再返回最终得到的响应字符串，Flask使用Jinja2模板引擎渲染模板。

Jinja2提供很多过滤器来修改变量，**注意：千万别在不可信的值上使用safe过滤器，例如用户在表单中输入的文本**

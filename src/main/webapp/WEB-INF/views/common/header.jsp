<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>星拍 · 短视频器材租赁与排期</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="topbar">
    <div class="topbar-inner">
        <div class="brand"><span class="logo">SP</span><span>星拍器材 · 租赁与排期</span></div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/equipment-list.action">器材台账</a>
            <a href="${pageContext.request.contextPath}/rental-list.action">租赁订单</a>
            <a href="${pageContext.request.contextPath}/schedule.action">档期排期</a>
            <a class="nav-cta" href="${pageContext.request.contextPath}/rental-form.action">快速下单</a>
        </nav>
    </div>
</header>
<main class="container">
<s:if test="flashMessage != null && flashMessage != ''">
    <div class="alert alert-<s:property value='flashType'/>"><s:property value="flashMessage"/></div>
</s:if>
<s:if test="hasActionErrors()">
    <div class="alert alert-error"><s:actionerror/></div>
</s:if>

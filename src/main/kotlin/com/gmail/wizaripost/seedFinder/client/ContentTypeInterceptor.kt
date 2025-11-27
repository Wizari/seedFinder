package com.gmail.wizaripost.seedFinder.client

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class ContentTypeInterceptor : RequestInterceptor {

    companion object {
        const val CONTENT_TYPE_HEADER = "Content-Type"
        const val VND_API_JSON = "application/vnd.api+json"
        const val APPLICATION_JSON = "application/json"
        const val CUSTOM_CONTENT_TYPE_HEADER = "X-Custom-Content-Type"
    }

    override fun apply(template: RequestTemplate) {
        val customType = template.headers()[CUSTOM_CONTENT_TYPE_HEADER]?.firstOrNull()
        if (customType != null) {
            template.removeHeader(CUSTOM_CONTENT_TYPE_HEADER)
            template.header(CONTENT_TYPE_HEADER, customType)
        } else {
            // Устанавливаем application/json по умолчанию, если не указано иное
            if (template.headers()[CONTENT_TYPE_HEADER]?.isEmpty() != false) {
                template.header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            }
        }
    }
}
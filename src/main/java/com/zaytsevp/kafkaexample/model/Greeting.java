package com.zaytsevp.kafkaexample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Простой объект (POJO) для пересылки
 *
 * @author Pavel Zaytsev
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {

    private String message;

    private String name;
}

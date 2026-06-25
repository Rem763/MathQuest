package com.quest.service;

import com.quest.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
    private final Random random = new Random();

    public Question generate(int level) {
        int difficulty = Math.max(1, Math.min(level, 5));
        int operandA;
        int operandB;
        char operator;
        int answer;

        switch (difficulty) {
            case 1 -> {
                operator = random.nextBoolean() ? '+' : '-';
                operandA = random.nextInt(10);
                operandB = random.nextInt(10);
                if (operator == '-') {
                    if (operandA < operandB) {
                        int temp = operandA;
                        operandA = operandB;
                        operandB = temp;
                    }
                    answer = operandA - operandB;
                } else {
                    answer = operandA + operandB;
                }
            }
            case 2 -> {
                operator = random.nextBoolean() ? '+' : '-';
                operandA = random.nextInt(20);
                operandB = random.nextInt(20);
                if (operator == '-') {
                    if (operandA < operandB) {
                        int temp = operandA;
                        operandA = operandB;
                        operandB = temp;
                    }
                    answer = operandA - operandB;
                } else {
                    answer = operandA + operandB;
                }
            }
            case 3 -> {
                operator = random.nextBoolean() ? '+' : '-';
                operandA = random.nextInt(100);
                operandB = random.nextInt(100);
                if (operator == '-') {
                    if (operandA < operandB) {
                        int temp = operandA;
                        operandA = operandB;
                        operandB = temp;
                    }
                    answer = operandA - operandB;
                } else {
                    answer = operandA + operandB;
                }
            }
            case 4 -> {
                operator = '×';
                operandA = random.nextInt(10) + 1;
                operandB = random.nextInt(10) + 1;
                answer = operandA * operandB;
            }
            default -> {
                int op = random.nextInt(4);
                operator = switch (op) {
                    case 0 -> '+';
                    case 1 -> '-';
                    case 2 -> '×';
                    default -> '÷';
                };
                if (operator == '÷') {
                    operandB = random.nextInt(9) + 1;
                    answer = random.nextInt(12) + 1;
                    operandA = operandB * answer;
                } else if (operator == '×') {
                    operandA = random.nextInt(12) + 1;
                    operandB = random.nextInt(12) + 1;
                    answer = operandA * operandB;
                } else if (operator == '-') {
                    operandA = random.nextInt(50) + 1;
                    operandB = random.nextInt(30) + 1;
                    if (operandA < operandB) {
                        int temp = operandA;
                        operandA = operandB;
                        operandB = temp;
                    }
                    answer = operandA - operandB;
                } else {
                    operandA = random.nextInt(50) + 1;
                    operandB = random.nextInt(30) + 1;
                    answer = operandA + operandB;
                }
            }
        }

        List<Integer> options = new ArrayList<>();
        options.add(answer);
        while (options.size() < 4) {
            int offset = random.nextInt(Math.max(3, difficulty * 4)) + 1;
            int candidate = random.nextBoolean() ? answer + offset : answer - offset;
            if (candidate >= 0 && !options.contains(candidate)) {
                options.add(candidate);
            }
        }
        Collections.shuffle(options, random);

        Question question = new Question();
        question.setText(operandA + " " + operator + " " + operandB + " = ?");
        question.setAnswer(answer);
        question.setOptions(options);
        return question;
    }
}

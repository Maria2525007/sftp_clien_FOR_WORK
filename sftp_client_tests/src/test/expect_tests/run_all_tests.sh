#!/bin/bash

# Проверка, установлена ли утилита Expect
if ! command -v expect &> /dev/null
then
    echo "Утилита Expect не установлена, установка..."
    sudo apt-get update
    sudo apt-get install -y expect
else
    echo "Утилита Expect уже установлена."
fi

# Запуск тестов
echo "Запуск теста cnct_and_cmds_test..."
expect ./cnct_and_cmds_test.exp

if [ $? -ne 0 ]; then
    echo "Тест cnct_and_cmds_test не прошел!"
    exit 1
fi

echo "Запуск теста persistence_test..."
expect ./persistence_test.exp

if [ $? -ne 0 ]; then
    echo "Тест persistence_test не прошел!"
    exit 1
fi

echo "Все тесты успешно завершены!"

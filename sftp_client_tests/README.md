Инструкция по запуску тестов:

1) Основные тесты на TestNG
Сборка - 
    cd sftp_client_tests
    mvn clean install
Запуск -
   mvn test

Тесты направлены на проверку основных функций клиента:
SFTPClient - эмулирование поключения к серверу с помощью заглушки SFTPClientStub, функции чтения / записи файла
JsonParserTest - парсинг / сериализация данных в / из тестого файла
ValidatorTest - валидация вводимых данных

2) Expect тесты
    cd src/main/test/expect_tests
   1) Запуск тестов по отдельности:
      (должна быть установления утилита Expect: sudo apt-get install expect)

   expect ./cnct_and_cmds_test.exp
   expect ./persistence_test.exp

   Каждый скрипт выведет в консоль сообщение с результатом тестом.
   Если тест завершается с кодом ошибки (exit 1), это означает, что тест не пройден.

   2) Для запуска всех тестов последовательно:
      ./run_all_tests.sh
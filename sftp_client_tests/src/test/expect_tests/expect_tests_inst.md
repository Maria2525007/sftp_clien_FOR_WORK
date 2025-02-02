1) Должна быть установления утилита Expect
   sudo apt-get update
   sudo apt-get install expect

2) Запуск тестов по отдельности
   ./test_duplicate_domain.exp
   ./test_invalid_domain.exp
   ./test_invalid_ip.exp
   ./test_sorted_list.exp

Каждый скрипт выведет в консоль 
сообщение с результатом (PASS или FAIL). 
Если тест завершается с кодом ошибки (exit 1),
это означает, что тест не прошёл.

3) Для запуска всех тестов последовательно
   chmod +x run_all_tests.sh
   ./run_all_tests.sh




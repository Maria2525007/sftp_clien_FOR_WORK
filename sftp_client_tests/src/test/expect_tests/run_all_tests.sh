#!/bin/bash
for test in *.exp; do
  echo "Запуск теста $test..."
  ./$test
  if [ $? -ne 0 ]; then
    echo "Тест $test завершился с ошибкой."
    exit 1
  fi
done
echo "Все тесты успешно пройдены."

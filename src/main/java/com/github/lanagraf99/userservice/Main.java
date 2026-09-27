package com.github.lanagraf99.userservice;

import com.github.lanagraf99.userservice.dao.UserDao;
import com.github.lanagraf99.userservice.dao.UserDaoImpl;
import com.github.lanagraf99.userservice.model.User;
import com.github.lanagraf99.userservice.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        User user = createUser(scanner);
                        userDao.save(user);
                        System.out.println("Сохранен: " + user);
                        break;
                    case 2:
                        User found = searchUser(scanner, userDao);
                        if (found != null) {
                            System.out.println("Найден: " + found);
                        }
                        break;
                    case 3:
                        System.out.println("Все пользователи:");
                        userDao.findAll().forEach(System.out::println);
                        break;
                    case 4:
                        User toUpdate = searchUser(scanner, userDao);
                        if (toUpdate == null) {
                            break;
                        }

                        updateUser(scanner, toUpdate);
                        userDao.update(toUpdate);
                        System.out.println("Обновлен: " + toUpdate);
                        break;
                    case 5:
                        User toDelete = searchUser(scanner, userDao);
                        if (toDelete == null) {
                            break;
                        }
                        userDao.delete(toDelete.getId());
                        System.out.println("Удален: " + toDelete);
                        break;
                    case 6:
                        System.out.println("Выход...");
                        HibernateUtil.shutdown();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный выбор");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число, а не буквы");
                scanner.nextLine();
            } catch (ConstraintViolationException e) {
                System.out.println("Ошибка: нарушено ограничение БД (дубликат email)");
            } catch (HibernateException e) {
                System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
            }
        }
    }

    public static void printMenu() {
        System.out.println("=== User Service ===");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти по id");
        System.out.println("3. Показать всех");
        System.out.println("4. Обновить");
        System.out.println("5. Удалить");
        System.out.println("6. Выход");
        System.out.println("Выберите действие:");
    }

    public static User createUser(Scanner scanner) {
        System.out.println("Введите имя: ");
        String name = scanner.nextLine();

        System.out.println("Введите email: ");
        String email = scanner.nextLine();

        System.out.println("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        return new User(name, email, age);
    }

    public static User searchUser(Scanner scanner, UserDao userDao) {
        System.out.println("Введите id: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь с id = " + id + " не найден");
            return null;
        }
        return user;
    }

    public static void updateUser(Scanner scanner, User toUpdate) {
        System.out.println("Новое имя: ");
        toUpdate.setName(scanner.nextLine());

        System.out.println("Новый email: ");
        toUpdate.setEmail(scanner.nextLine());

        System.out.println("Новый возраст: ");
        toUpdate.setAge(scanner.nextInt());
        scanner.nextLine();
    }
}
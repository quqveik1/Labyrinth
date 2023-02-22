package com.kurlic.labirints.view.Labyrinth;

import java.util.Random;

public class LabyrinthGenerator
{
    private int width;
    private int height;
    private boolean[][][] maze;

    public LabyrinthGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        maze = new boolean[width][height][4];
        // Создаем массив boolean, чтобы хранить информацию о стенах лабиринта
        // Размер массива - width x height x 4, где 4 - количество стен клетки
    }

    public void generateMaze() {
        Random random = new Random();
        // Выбираем случайную начальную клетку
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        // Вызываем метод для генерации лабиринта, начиная с этой клетки
        generateMaze(startX, startY);
    }

    private void generateMaze(int x, int y) {
        // Отмечаем текущую клетку как посещенную
        maze[x][y][3] = true;

        // Список направлений, в которых можно продвинуться из текущей клетки
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        // Тасуем направления, чтобы выбирать их в случайном порядке
        shuffleArray(directions);

        // Проходимся по всем направлениям
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                // Новая клетка выходит за границы лабиринта, пропускаем ее
                continue;
            }
            if (maze[newX][newY][3]) {
                // Новая клетка уже посещена, пропускаем ее
                continue;
            }

            // Удаляем стену между текущей клеткой и новой клеткой
            if (direction[0] == 1) {
                maze[x][y][1] = false;
                maze[newX][newY][3] = false;
            } else if (direction[0] == -1) {
                maze[x][y][3] = false;
                maze[newX][newY][1] = false;
            }
            if (direction[1] == 1) {
                maze[x][y][2] = false;
                maze[newX][newY][0] = false;
            } else if (direction[1] == -1) {
                maze[x][y][0] = false;
                maze[newX][newY][2] = false;
            }

            // Рекурсивно генерируем лабиринт, начиная с новой клетки
            generateMaze(newX, newY);
        }

        // Установим толстые стены для текущей клетки
        for (int i = 0; i < 4; i++) {
            if (maze[x][y][i]) {
                // Если стена уже существует, делаем ее толстой
                maze[x][y][i] = false;
                maze[x][y][(i + 1) % 4] = false;
            }
        }
    }

    // Метод для перемешивания массива направлений
    private void shuffleArray(int[][] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}

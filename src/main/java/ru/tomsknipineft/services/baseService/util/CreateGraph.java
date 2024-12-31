// Класс для выгрузки данных из БД в виде графика/диаграммы

package ru.tomsknipineft.services.baseService.util;

import jakarta.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.tomsknipineft.entities.TaskSchedule;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CreateGraph {

    /**
     * Формирование графика данных
     * @param response HttpServletResponse
     * @param graphName Название графика
     * @param axisX Название оси X
     * @param axisY Название оси Y
     * @param dataset набор данных лдя графика
     */
    public void getChart(HttpServletResponse response, String graphName, String axisX, String axisY, DefaultCategoryDataset dataset) {
        // Создание графика
        JFreeChart chart = ChartFactory.createLineChart(
                graphName, // Заголовок диаграммы
                axisX, // Ось X
                axisY, // Ось Y
                dataset
        );
        // Установка прозрачного фона
        chart.setBackgroundPaint(new Color(0, 0, 0, 0)); // Устанавливаем прозрачный фон

        // Получаем область графика и делаем её фоном прозрачным
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0, 0, 0, 0)); // Прозрачный фон для области графика
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY); // Цвет линий сетки
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY); // Цвет линий сетки
        plot.setOutlineVisible(false);
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        plot.setRenderer(renderer);

        // Установка типа ответа
        response.setContentType("image/png");

        // Генерация графика и запись его в ответ
        OutputStream out;
        try {
            out = response.getOutputStream();
            ChartUtils.writeChartAsPNG(out, chart, 800, 600);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Подготовка набора данных для графика
     * @param tasks Список задач
     * @param dataX значение параметра Х
     * @param dataset набор данных для пополнения по dataX
     * @return поплненный набор данных для dataX
     */
    public DefaultCategoryDataset createDataset(List<TaskSchedule> tasks, Double dataX, DefaultCategoryDataset dataset) {
        // Добавление данных в набор
        for (TaskSchedule task : tasks) {
            dataset.addValue(task.getTaskDuration(), task.getTaskName(), dataX);
        }
        return dataset;
    }
}

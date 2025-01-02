// Класс для выгрузки данных из БД в виде графика/диаграммы

package ru.tomsknipineft.services.baseService.util;

import jakarta.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
    public void getChart(HttpServletResponse response, String graphName, String axisX, String axisY, XYSeriesCollection dataset) {
        // Создание графика
        JFreeChart chart = ChartFactory.createXYLineChart
                (
                graphName, // Заголовок диаграммы
                axisX, // Ось X
                axisY, // Ось Y
                dataset
        );
        // Установка прозрачного фона
        chart.setBackgroundPaint(new Color(0, 0, 0, 0)); // Устанавливаем прозрачный фон

        // Получаем область графика и делаем её фоном прозрачным
        XYPlot xyPlot = (XYPlot) chart.getPlot(); // Получаем XYPlot
        xyPlot.setBackgroundPaint(new Color(0, 0, 0, 0)); // Прозрачный фон для области графика
        xyPlot.setOutlineVisible(false); // Убираем рамку вокруг графика

        // Настройка осей
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis(); // Ось X
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis(); // Ось Y
        domain.setLabel("Протяженность автодороги, км");
        range.setLabel("Трудозатраты, чел/дн");
        // Применение числового масштаба
        domain.setAutoRangeIncludesZero(false); // Не включать 0 в диапазон
        range.setAutoRangeIncludesZero(false); // Не включать 0 в диапазон

        xyPlot.setDomainGridlinePaint(Color.LIGHT_GRAY); // Цвет линий сетки
        xyPlot.setRangeGridlinePaint(Color.LIGHT_GRAY); // Цвет линий сетки

        // Настройка рендерера
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        xyPlot.setRenderer(renderer);

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
    public XYSeriesCollection createDataset(List<TaskSchedule> tasks, Double dataX, XYSeriesCollection dataset) {
        // Добавление данных в набор
        if (dataset.getSeries().isEmpty()){
            for (TaskSchedule task : tasks) {
                XYSeries dataSeries = new XYSeries(task.getTaskName());
                dataSeries.add(dataX, task.getTaskDuration());
                dataset.addSeries(dataSeries);
            }
        }
        else {
            for (TaskSchedule task : tasks) {
                dataset.getSeries(task.getTaskName()).add(dataX, task.getTaskDuration());
            }
        }

        return dataset;
    }
}

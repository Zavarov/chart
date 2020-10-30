package vartas.chart.pie;

import vartas.chart.pie.$factory.NumberDatasetFactory;

import java.awt.*;

public abstract class PieChart extends PieChartTOP{
    public void putEntries(String key, Number value){
        this.putEntries(key, value, new Color(key.hashCode() & 0xFFFFF));
    }

    public void putEntries(String key, Number value, Color color){
        super.putEntries(key, NumberDatasetFactory.create(value, color));
    }

    @Override
    public PieChart getRealThis(){
        return this;
    }
}

package vartas.chart.line;

import vartas.chart.line.$factory.NumberDatasetFactory;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

public abstract class LineChart extends LineChartTOP{
    public void addEntries(@Nonnull Number value, @Nonnull LocalDateTime created, @Nonnull String label){
        super.addEntries(NumberDatasetFactory.create(value, created, label, Position.LEFT));
    }

    public void addEntries(@Nonnull Number value, @Nonnull LocalDateTime created, @Nonnull String label, @Nonnull Position position){
        super.addEntries(NumberDatasetFactory.create(value, created, label, position));
    }

    @Override
    public LineChart getRealThis(){
        return this;
    }
}

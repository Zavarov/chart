package vartas.chart.line;

public class ListDataset extends ListDatasetTOP{
    @Override
    public Double getCount() {
        return (double) this.sizeEntries();
    }

    @Override
    public ListDataset getRealThis() {
        return this;
    }
}

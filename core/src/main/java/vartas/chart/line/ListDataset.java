package vartas.chart.line;

public class ListDataset extends ListDatasetTOP{
    @Override
    public Number getCount() {
        return this.sizeEntries();
    }

    @Override
    public ListDataset getRealThis() {
        return this;
    }
}

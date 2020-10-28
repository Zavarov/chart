package vartas.chart.pie;

public class ListDataset extends ListDatasetTOP {
    @Override
    public long getCount() {
        return this.sizeEntries();
    }

    @Override
    public ListDataset getRealThis() {
        return this;
    }
}

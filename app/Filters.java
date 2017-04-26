import play.mvc.EssentialFilter;
import play.http.DefaultHttpFilters;
import play.filters.cors.CORSFilter;
import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject public Filters(CORSFilter corsFilter) {
        super(corsFilter);
    }
}
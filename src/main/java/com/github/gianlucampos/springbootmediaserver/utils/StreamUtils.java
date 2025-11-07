package com.github.gianlucampos.springbootmediaserver.utils;

import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;

@UtilityClass
public class StreamUtils {

    public ResourceRegion resourceRegion(Resource resource, HttpHeaders headers, long chunkSize) throws IOException {
        long contentLength = resource.contentLength();
        List<HttpRange> httpRanges = headers.getRange();

        if (httpRanges.isEmpty()) {
            long count = Math.min(chunkSize, contentLength);
            return new ResourceRegion(resource, 0, count);
        }

        HttpRange range = httpRanges.getFirst();
        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);
        long rangeLength = Math.min(chunkSize, end - start + 1);

        return new ResourceRegion(resource, start, rangeLength);
    }

}

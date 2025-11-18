package az.devlab.paysnapservice.serviceimpl;

import az.devlab.paysnapservice.exception.NotFoundException;
import az.devlab.paysnapservice.model.ShortUrl;
import az.devlab.paysnapservice.repository.ShortUrlRepository;
import az.devlab.paysnapservice.service.UrlShortenerService;
import az.devlab.paysnapservice.util.ShortUrlGenerator;
import az.devlab.paysnapservice.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    private static final String BASE_URL = "https://paysnap.dev/s/";

    @Override
    public String createShortUrlForPayment(String originalUrl) {
        String code;

        do {
            code = ShortUrlGenerator.generateShortCode(8);
        } while (shortUrlRepository.existsByShortCode(code));

        ShortUrl shortUrl = ShortUrl.builder()
                .shortCode(code)
                .originalUrl(originalUrl)
                .createdAt(TimeUtils.now())
                .build();

        shortUrlRepository.save(shortUrl);

        return BASE_URL + code;
    }

    @Override
    public String resolveOriginalUrl(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new NotFoundException("Short URL not found"));

        return shortUrl.getOriginalUrl();
    }
}

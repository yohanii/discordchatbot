CREATE TABLE IF NOT EXISTS quote (
    quote_id INT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(50) NOT NULL,
    quote VARCHAR(300) NOT NULL
);

INSERT INTO quote (author, quote) VALUES
('Albert Einstein', 'Life is like riding a bicycle. To keep your balance you must keep moving.'),
('Maya Angelou', 'You will face many defeats in life, but never let yourself be defeated.'),
('Mark Twain', 'The secret of getting ahead is getting started.'),
('Oscar Wilde', 'Be yourself; everyone else is already taken.'),
('Nelson Mandela', 'It always seems impossible until it’s done.');
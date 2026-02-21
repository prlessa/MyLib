package com.mylib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    private static ConexaoDB instancia;
    private Connection conexao;

    private ConexaoDB() {
        try {
            conexao = DriverManager.getConnection("jdbc:sqlite:mylib.db");
            inicializarBanco();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public static ConexaoDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoDB();
        }
        return instancia;
    }

    public Connection getConexao() {
        return conexao;
    }

    private void inicializarBanco() throws SQLException {
        Statement stmt = conexao.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS livros (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo    TEXT NOT NULL,
                autor     TEXT NOT NULL,
                genero    TEXT,
                ano       INTEGER,
                status    TEXT DEFAULT 'META',
                avaliacao INTEGER DEFAULT 0,
                na_estante INTEGER DEFAULT 0
            )
        """);

        BancoDadosInicial(stmt);
    }

    private void BancoDadosInicial(Statement stmt) throws SQLException {
        var rs = stmt.executeQuery("SELECT COUNT(*) FROM livros");
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("""
            INSERT INTO livros (titulo, autor, genero, ano) VALUES
            -- Clássicos da Literatura Mundial
            ('Dom Quixote', 'Miguel de Cervantes', 'Romance', 1605),
            ('Hamlet', 'William Shakespeare', 'Teatro', 1603),
            ('A Divina Comédia', 'Dante Alighieri', 'Poesia', 1320),
            ('Odisseia', 'Homero', 'Épico', -800),
            ('Ilíada', 'Homero', 'Épico', -750),
            ('Os Miseráveis', 'Victor Hugo', 'Romance', 1862),
            ('Guerra e Paz', 'Leon Tolstói', 'Romance', 1869),
            ('Anna Karenina', 'Leon Tolstói', 'Romance', 1878),
            ('Crime e Castigo', 'Fiódor Dostoiévski', 'Romance', 1866),
            ('O Idiota', 'Fiódor Dostoiévski', 'Romance', 1869),
            ('Os Irmãos Karamázov', 'Fiódor Dostoiévski', 'Romance', 1880),
            ('Madame Bovary', 'Gustave Flaubert', 'Romance', 1857),
            ('Em Busca do Tempo Perdido', 'Marcel Proust', 'Romance', 1913),
            ('Ulisses', 'James Joyce', 'Romance', 1922),
            ('O Processo', 'Franz Kafka', 'Romance', 1925),
            ('A Metamorfose', 'Franz Kafka', 'Conto', 1915),
            ('O Castelo', 'Franz Kafka', 'Romance', 1926),
            ('Moby Dick', 'Herman Melville', 'Romance', 1851),
            ('As Aventuras de Huckleberry Finn', 'Mark Twain', 'Romance', 1884),
            ('O Retrato de Dorian Gray', 'Oscar Wilde', 'Romance', 1890),
            ('A Importância de ser Honesto', 'Oscar Wilde', 'Teatro', 1895),
            ('Orgulho e Preconceito', 'Jane Austen', 'Romance', 1813),
            ('Razão e Sensibilidade', 'Jane Austen', 'Romance', 1811),
            ('Emma', 'Jane Austen', 'Romance', 1815),
            ('Jane Eyre', 'Charlotte Brontë', 'Romance', 1847),
            ('O Morro dos Ventos Uivantes', 'Emily Brontë', 'Romance', 1847),
            ('David Copperfield', 'Charles Dickens', 'Romance', 1850),
            ('Oliver Twist', 'Charles Dickens', 'Romance', 1838),
            ('Grandes Esperanças', 'Charles Dickens', 'Romance', 1861),
            ('A Tale of Two Cities', 'Charles Dickens', 'Romance', 1859),
            ('Middlemarch', 'George Eliot', 'Romance', 1872),
            ('Vanity Fair', 'William Makepeace Thackeray', 'Romance', 1848),
            ('Robinson Crusoe', 'Daniel Defoe', 'Aventura', 1719),
            ('As Viagens de Gulliver', 'Jonathan Swift', 'Sátira', 1726),
            ('Tom Jones', 'Henry Fielding', 'Romance', 1749),
            ('O Vermelho e o Negro', 'Stendhal', 'Romance', 1830),
            ('A Cartuxa de Parma', 'Stendhal', 'Romance', 1839),
            ('Pai Goriot', 'Honoré de Balzac', 'Romance', 1835),
            ('Eugénie Grandet', 'Honoré de Balzac', 'Romance', 1833),
            ('Germinal', 'Émile Zola', 'Romance', 1885),
            ('Nana', 'Émile Zola', 'Romance', 1880),
            ('Contos de Hoffmann', 'E.T.A. Hoffmann', 'Fantástico', 1816),
            ('Fausto', 'Johann Wolfgang von Goethe', 'Teatro', 1808),
            ('Os Sofrimentos do Jovem Werther', 'Johann Wolfgang von Goethe', 'Romance', 1774),
            ('Ifigênia em Táuride', 'Eurípides', 'Teatro', -414),
            ('Édipo Rei', 'Sófocles', 'Teatro', -429),
            ('A Eneida', 'Virgílio', 'Épico', -19),
            ('As Metamorfoses', 'Ovídio', 'Poesia', 8),
            ('Decamerão', 'Giovanni Boccaccio', 'Conto', 1353),
            ('Os Lusíadas', 'Luís de Camões', 'Épico', 1572),
            -- Literatura Brasileira
            ('Dom Casmurro', 'Machado de Assis', 'Romance', 1899),
            ('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance', 1881),
            ('O Alienista', 'Machado de Assis', 'Novela', 1882),
            ('Quincas Borba', 'Machado de Assis', 'Romance', 1891),
            ('O Cortiço', 'Aluísio Azevedo', 'Romance', 1890),
            ('A Moreninha', 'Joaquim Manuel de Macedo', 'Romance', 1844),
            ('Iracema', 'José de Alencar', 'Romance', 1865),
            ('O Guarani', 'José de Alencar', 'Romance', 1857),
            ('Grande Sertão: Veredas', 'João Guimarães Rosa', 'Romance', 1956),
            ('Sagarana', 'João Guimarães Rosa', 'Conto', 1946),
            ('A Hora da Estrela', 'Clarice Lispector', 'Romance', 1977),
            ('Perto do Coração Selvagem', 'Clarice Lispector', 'Romance', 1943),
            ('A Paixão Segundo G.H.', 'Clarice Lispector', 'Romance', 1964),
            ('Vidas Secas', 'Graciliano Ramos', 'Romance', 1938),
            ('São Bernardo', 'Graciliano Ramos', 'Romance', 1934),
            ('Capitães da Areia', 'Jorge Amado', 'Romance', 1937),
            ('Gabriela, Cravo e Canela', 'Jorge Amado', 'Romance', 1958),
            ('Dona Flor e Seus Dois Maridos', 'Jorge Amado', 'Romance', 1966),
            ('Triste Fim de Policarpo Quaresma', 'Lima Barreto', 'Romance', 1915),
            ('O Ateneu', 'Raul Pompeia', 'Romance', 1888),
            ('Macunaíma', 'Mário de Andrade', 'Romance', 1928),
            ('Serafim Ponte Grande', 'Oswald de Andrade', 'Romance', 1933),
            ('A Bagaceira', 'José Américo de Almeida', 'Romance', 1928),
            ('Fogo Morto', 'José Lins do Rego', 'Romance', 1943),
            ('O Quinze', 'Rachel de Queiroz', 'Romance', 1930),
            ('Morte e Vida Severina', 'João Cabral de Melo Neto', 'Poesia', 1955),
            ('Lavoura Arcaica', 'Raduan Nassar', 'Romance', 1975),
            ('A Pedra do Reino', 'Ariano Suassuna', 'Romance', 1971),
            ('Menino de Engenho', 'José Lins do Rego', 'Romance', 1932),
            ('Éramos Seis', 'Maria José Dupré', 'Romance', 1943),
            -- Literatura Norte-Americana
            ('1984', 'George Orwell', 'Distopia', 1949),
            ('A Revolução dos Bichos', 'George Orwell', 'Fábula', 1945),
            ('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fábula', 1943),
            ('Cem Anos de Solidão', 'Gabriel García Márquez', 'Realismo Mágico', 1967),
            ('O Amor nos Tempos do Cólera', 'Gabriel García Márquez', 'Romance', 1985),
            ('O Senhor dos Anéis', 'J.R.R. Tolkien', 'Fantasia', 1954),
            ('O Hobbit', 'J.R.R. Tolkien', 'Fantasia', 1937),
            ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', 1997),
            ('O Apanhador no Campo de Centeio', 'J.D. Salinger', 'Romance', 1951),
            ('O Grande Gatsby', 'F. Scott Fitzgerald', 'Romance', 1925),
            ('Tender is the Night', 'F. Scott Fitzgerald', 'Romance', 1934),
            ('Por Quem os Sinos Dobram', 'Ernest Hemingway', 'Romance', 1940),
            ('O Velho e o Mar', 'Ernest Hemingway', 'Novela', 1952),
            ('As I Lay Dying', 'William Faulkner', 'Romance', 1930),
            ('O Som e a Fúria', 'William Faulkner', 'Romance', 1929),
            ('Grapes of Wrath', 'John Steinbeck', 'Romance', 1939),
            ('De Camundongos e Homens', 'John Steinbeck', 'Novela', 1937),
            ('Lolita', 'Vladimir Nabokov', 'Romance', 1955),
            ('Invisible Man', 'Ralph Ellison', 'Romance', 1952),
            ('Beloved', 'Toni Morrison', 'Romance', 1987),
            ('O Dom da Morte', 'Toni Morrison', 'Romance', 1973),
            ('Fahrenheit 451', 'Ray Bradbury', 'Distopia', 1953),
            ('Admirável Mundo Novo', 'Aldous Huxley', 'Distopia', 1932),
            ('O Senhor das Moscas', 'William Golding', 'Romance', 1954),
            ('Catch-22', 'Joseph Heller', 'Sátira', 1961),
            ('To Kill a Mockingbird', 'Harper Lee', 'Romance', 1960),
            ('On the Road', 'Jack Kerouac', 'Romance', 1957),
            ('Howl', 'Allen Ginsberg', 'Poesia', 1956),
            ('The Bell Jar', 'Sylvia Plath', 'Romance', 1963),
            ('One Flew Over the Cuckoo''s Nest', 'Ken Kesey', 'Romance', 1962),
            -- Literatura Latino-Americana e Europeia
            ('Ficciones', 'Jorge Luis Borges', 'Conto', 1944),
            ('O Aleph', 'Jorge Luis Borges', 'Conto', 1949),
            ('Rayuela', 'Julio Cortázar', 'Romance', 1963),
            ('La Casa de los Espíritus', 'Isabel Allende', 'Realismo Mágico', 1982),
            ('Pedro Páramo', 'Juan Rulfo', 'Romance', 1955),
            ('El Túnel', 'Ernesto Sabato', 'Romance', 1948),
            ('Conversación en la Catedral', 'Mario Vargas Llosa', 'Romance', 1969),
            ('La Ciudad y los Perros', 'Mario Vargas Llosa', 'Romance', 1963),
            ('O Nome da Rosa', 'Umberto Eco', 'Romance', 1980),
            ('O Pêndulo de Foucault', 'Umberto Eco', 'Romance', 1988),
            ('Se um Viajante numa Noite de Inverno', 'Italo Calvino', 'Romance', 1979),
            ('As Cidades Invisíveis', 'Italo Calvino', 'Romance', 1972),
            ('O Leopardo', 'Giuseppe Tomasi di Lampedusa', 'Romance', 1958),
            ('A Montanha Mágica', 'Thomas Mann', 'Romance', 1924),
            ('Morte em Veneza', 'Thomas Mann', 'Novela', 1912),
            ('O Tambor de Lata', 'Günter Grass', 'Romance', 1959),
            ('O Deserto dos Tártaros', 'Dino Buzzati', 'Romance', 1940),
            ('Sidarta', 'Hermann Hesse', 'Romance', 1922),
            ('O Lobo da Estepe', 'Hermann Hesse', 'Romance', 1927),
            ('Demian', 'Hermann Hesse', 'Romance', 1919),
            ('Narciso e Goldmund', 'Hermann Hesse', 'Romance', 1930),
            ('O Estrangeiro', 'Albert Camus', 'Romance', 1942),
            ('A Peste', 'Albert Camus', 'Romance', 1947),
            ('A Náusea', 'Jean-Paul Sartre', 'Romance', 1938),
            ('O Segundo Sexo', 'Simone de Beauvoir', 'Ensaio', 1949),
            ('Esperando Godot', 'Samuel Beckett', 'Teatro', 1953),
            ('O Rinoceronte', 'Eugène Ionesco', 'Teatro', 1959),
            ('As Flores do Mal', 'Charles Baudelaire', 'Poesia', 1857),
            ('Uma Estação no Inferno', 'Arthur Rimbaud', 'Poesia', 1873),
            ('Poemas em Prosa', 'Stéphane Mallarmé', 'Poesia', 1897),
            ('Ensaio Sobre a Cegueira', 'José Saramago', 'Romance', 1995),
            ('Memorial do Convento', 'José Saramago', 'Romance', 1982),
            ('O Evangelho Segundo Jesus Cristo', 'José Saramago', 'Romance', 1991),
            ('Pessoa Insubmissa', 'Fernando Pessoa', 'Poesia', 1934),
            ('O Livro do Desassossego', 'Fernando Pessoa', 'Prosa Poética', 1982),
            ('Ficções', 'Fernando Pessoa', 'Poesia', 1946),
            ('Clarissa', 'Samuel Richardson', 'Romance', 1748),
            ('O Médico e o Monstro', 'Robert Louis Stevenson', 'Novela', 1886),
            ('Drácula', 'Bram Stoker', 'Terror', 1897),
            ('Frankenstein', 'Mary Shelley', 'Terror', 1818),
            ('Alice no País das Maravilhas', 'Lewis Carroll', 'Fantasia', 1865),
            ('Através do Espelho', 'Lewis Carroll', 'Fantasia', 1871),
            ('A Ilha do Tesouro', 'Robert Louis Stevenson', 'Aventura', 1883),
            ('O Conde de Monte Cristo', 'Alexandre Dumas', 'Aventura', 1844),
            ('Os Três Mosqueteiros', 'Alexandre Dumas', 'Aventura', 1844),
            ('Vinte Mil Léguas Submarinas', 'Júlio Verne', 'Ficção Científica', 1870),
            ('A Volta ao Mundo em 80 Dias', 'Júlio Verne', 'Aventura', 1872),
            ('Da Terra à Lua', 'Júlio Verne', 'Ficção Científica', 1865),
            ('A Guerra dos Mundos', 'H.G. Wells', 'Ficção Científica', 1898),
            ('A Máquina do Tempo', 'H.G. Wells', 'Ficção Científica', 1895),
            -- Século XXI
            ('As Correções', 'Jonathan Franzen', 'Romance', 2001),
            ('Liberdade', 'Jonathan Franzen', 'Romance', 2010),
            ('A Estrada', 'Cormac McCarthy', 'Romance', 2006),
            ('Não Me Abandones Jamais', 'Kazuo Ishiguro', 'Romance', 2005),
            ('O Resto do Dia', 'Kazuo Ishiguro', 'Romance', 1989),
            ('Austerlitz', 'W.G. Sebald', 'Romance', 2001),
            ('Expiação', 'Ian McEwan', 'Romance', 2001),
            ('Sábado', 'Ian McEwan', 'Romance', 2005),
            ('A Vida dos Outros', 'Ingo Schulze', 'Romance', 2005),
            ('O Leitor', 'Bernhard Schlink', 'Romance', 1995),
            ('A Elegância do Ouriço', 'Muriel Barbery', 'Romance', 2006),
            ('As Benevolentes', 'Jonathan Littell', 'Romance', 2006),
            ('O Perfume', 'Patrick Süskind', 'Romance', 1985),
            ('Middlesex', 'Jeffrey Eugenides', 'Romance', 2002),
            ('As Horas', 'Michael Cunningham', 'Romance', 1998),
            ('A Queda', 'Albert Camus', 'Romance', 1956),
            ('O Código Da Vinci', 'Dan Brown', 'Thriller', 2003),
            ('Inferno', 'Dan Brown', 'Thriller', 2013),
            ('A Menina que Roubava Livros', 'Markus Zusak', 'Romance', 2005),
            ('O Caçador de Pipas', 'Khaled Hosseini', 'Romance', 2003),
            ('Mil Sóis Esplêndidos', 'Khaled Hosseini', 'Romance', 2007),
            ('O Conto da Aia', 'Margaret Atwood', 'Distopia', 1985),
            ('Os Testamentos', 'Margaret Atwood', 'Distopia', 2019),
            ('Oryx e Crake', 'Margaret Atwood', 'Ficção Científica', 2003),
            ('Cloud Atlas', 'David Mitchell', 'Romance', 2004),
            ('A Caverna', 'José Saramago', 'Romance', 2000),
            ('As Intermitências da Morte', 'José Saramago', 'Romance', 2005),
            ('Ensaio Sobre a Lucidez', 'José Saramago', 'Romance', 2004),
            ('A Substância do Mal', 'Luca D''Andrea', 'Thriller', 2016),
            ('O Labirinto dos Espíritos', 'Carlos Ruiz Zafón', 'Romance', 2016),
            ('A Sombra do Vento', 'Carlos Ruiz Zafón', 'Romance', 2001),
            ('2666', 'Roberto Bolaño', 'Romance', 2004),
            ('Os Detetives Selvagens', 'Roberto Bolaño', 'Romance', 1998),
            ('A Solidão dos Números Primos', 'Paolo Giordano', 'Romance', 2008),
            ('A Amiga Genial', 'Elena Ferrante', 'Romance', 2011),
            ('História do Novo Sobrenome', 'Elena Ferrante', 'Romance', 2012),
            ('Aquele que Parte', 'Elena Ferrante', 'Romance', 2013),
            ('A Criança Perdida', 'Elena Ferrante', 'Romance', 2014),
            ('O Homem de Giz', 'C.J. Tudor', 'Thriller', 2018),
            ('Where the Crawdads Sing', 'Delia Owens', 'Romance', 2018),
            ('Normal People', 'Sally Rooney', 'Romance', 2018),
            ('Conversations with Friends', 'Sally Rooney', 'Romance', 2017),
            ('O Mundo de Ontem', 'Stefan Zweig', 'Memórias', 1942),
            ('Carta de uma Desconhecida', 'Stefan Zweig', 'Novela', 1922),
            ('Xadrez', 'Stefan Zweig', 'Novela', 1942),
            ('A Balada de Pássaros e Serpentes', 'Suzanne Collins', 'Distopia', 2020),
            ('Duna', 'Frank Herbert', 'Ficção Científica', 1965),
            ('Fundação', 'Isaac Asimov', 'Ficção Científica', 1951),
            ('Neuromancer', 'William Gibson', 'Ficção Científica', 1984),
            ('Sapiens', 'Yuval Noah Harari', 'Não-Ficção', 2011),
            ('Homo Deus', 'Yuval Noah Harari', 'Não-Ficção', 2015),
            ('21 Lições para o Século 21', 'Yuval Noah Harari', 'Não-Ficção', 2018)
        """);
        }
    }
}
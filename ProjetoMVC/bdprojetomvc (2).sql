-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Tempo de geração: 01/11/2025 às 22:12
-- Versão do servidor: 8.0.42
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `bdprojetomvc`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `aluno_disciplina`
--

CREATE TABLE `aluno_disciplina` (
  `id` int NOT NULL,
  `aluno_ra` int UNSIGNED NOT NULL,
  `id_curso_disciplina` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `aluno_disciplina`
--

INSERT INTO `aluno_disciplina` (`id`, `aluno_ra`, `id_curso_disciplina`) VALUES
(1, 1001, 1),
(2, 1001, 2),
(3, 1001, 3),
(4, 1002, 1),
(5, 1002, 2),
(6, 1003, 6),
(7, 1003, 7),
(8, 1003, 8),
(9, 1004, 1),
(10, 1004, 2),
(11, 1004, 3),
(12, 1005, 1),
(13, 1005, 2),
(14, 1005, 3),
(15, 1006, 1),
(16, 1006, 2),
(17, 1006, 3),
(18, 1007, 4),
(19, 1007, 5),
(20, 1008, 4),
(21, 1008, 5),
(22, 1009, 4),
(23, 1009, 5),
(24, 1010, 6),
(25, 1010, 7),
(26, 1010, 8),
(27, 1011, 6),
(28, 1011, 7),
(29, 1011, 8),
(30, 1012, 6),
(31, 1012, 7),
(32, 1012, 8),
(33, 1013, 1),
(34, 1013, 2),
(35, 1013, 3),
(36, 1014, 4),
(37, 1014, 5),
(38, 1015, 6),
(39, 1015, 7),
(40, 1015, 8),
(41, 1016, 1),
(42, 1016, 2),
(43, 1016, 3),
(44, 1017, 4),
(45, 1017, 5),
(46, 1018, 6),
(47, 1018, 7),
(48, 1018, 8);

-- --------------------------------------------------------

--
-- Estrutura para tabela `aluno_nota_falta`
--

CREATE TABLE `aluno_nota_falta` (
  `id` int NOT NULL,
  `id_aluno_disciplina` int NOT NULL,
  `nota1` decimal(5,2) DEFAULT NULL,
  `nota2` decimal(5,2) DEFAULT NULL,
  `faltas` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `aluno_nota_falta`
--

INSERT INTO `aluno_nota_falta` (`id`, `id_aluno_disciplina`, `nota1`, `nota2`, `faltas`) VALUES
(1, 1, 0.00, 0.00, 0),
(2, 2, 0.00, 0.00, 2),
(3, 3, 0.00, 0.00, 1),
(4, 4, 0.00, 0.00, 0),
(5, 5, 0.00, 0.00, 3),
(6, 6, 0.00, 0.00, 0),
(7, 7, 0.00, 0.00, 0),
(8, 8, 0.00, 0.00, 0),
(9, 9, 0.00, 0.00, 0),
(10, 10, 0.00, 0.00, 1),
(11, 11, 0.00, 0.00, 2),
(12, 12, 0.00, 0.00, 0),
(13, 13, 0.00, 0.00, 3),
(14, 14, 0.00, 0.00, 0),
(15, 15, 0.00, 0.00, 0),
(16, 16, 0.00, 0.00, 1),
(17, 17, 0.00, 0.00, 0),
(18, 18, 0.00, 0.00, 1),
(19, 19, 0.00, 0.00, 2),
(20, 20, 0.00, 0.00, 0),
(21, 21, 0.00, 0.00, 3),
(22, 22, 0.00, 0.00, 0),
(23, 23, 0.00, 0.00, 1),
(24, 24, 0.00, 0.00, 0),
(25, 25, 0.00, 0.00, 0),
(26, 26, 0.00, 0.00, 1),
(27, 27, 0.00, 0.00, 2),
(28, 28, 0.00, 0.00, 0),
(29, 29, 0.00, 0.00, 0),
(30, 30, 0.00, 0.00, 0),
(31, 31, 0.00, 0.00, 1),
(32, 32, 0.00, 0.00, 2),
(33, 33, 0.00, 0.00, 0),
(34, 34, 0.00, 0.00, 1),
(35, 35, 0.00, 0.00, 0),
(36, 36, 0.00, 0.00, 0),
(37, 37, 0.00, 0.00, 2),
(38, 38, 0.00, 0.00, 0),
(39, 39, 0.00, 0.00, 0),
(40, 40, 0.00, 0.00, 1),
(41, 41, 0.00, 0.00, 0),
(42, 42, 0.00, 0.00, 0),
(43, 43, 0.00, 0.00, 1),
(44, 44, 0.00, 0.00, 2),
(45, 45, 0.00, 0.00, 0),
(46, 46, 0.00, 0.00, 0),
(47, 47, 0.00, 0.00, 1),
(48, 48, 0.00, 0.00, 2);

-- --------------------------------------------------------

--
-- Estrutura para tabela `campus`
--

CREATE TABLE `campus` (
  `id_campus` int NOT NULL,
  `nome_campus` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cep` varchar(9) COLLATE utf8mb4_unicode_ci NOT NULL,
  `endereco` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numero` int UNSIGNED DEFAULT NULL,
  `cidade` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uf` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Despejando dados para a tabela `campus`
--

INSERT INTO `campus` (`id_campus`, `nome_campus`, `cep`, `endereco`, `numero`, `cidade`, `estado`, `uf`) VALUES
(1, 'Fatec Guarulhos', '07023-070', 'Av. Salgado Filho', 3500, 'Guarulhos', 'São Paulo', 'SP'),
(2, 'Fatec São Paulo', '01124-010', 'Av. Tiradentes', 600, 'São Paulo', 'São Paulo', 'SP');

-- --------------------------------------------------------

--
-- Estrutura para tabela `curso`
--

CREATE TABLE `curso` (
  `id_curso` int NOT NULL,
  `nome_curso` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_campus` int NOT NULL,
  `periodo` enum('Matutino','Vespertino','Noturno') COLLATE utf8mb4_unicode_ci NOT NULL,
  `duracao_semestre` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Despejando dados para a tabela `curso`
--

INSERT INTO `curso` (`id_curso`, `nome_curso`, `id_campus`, `periodo`, `duracao_semestre`) VALUES
(1, 'ADS - Análise e Desenvolvimento de Sistemas', 1, 'Noturno', 6),
(2, 'Logística', 1, 'Matutino', 6),
(3, 'ADS - Análise e Desenvolvimento de Sistemas', 2, 'Noturno', 6);

-- --------------------------------------------------------

--
-- Estrutura para tabela `curso_disciplina`
--

CREATE TABLE `curso_disciplina` (
  `id` int NOT NULL,
  `id_curso` int NOT NULL,
  `id_disciplina` int NOT NULL,
  `semestre` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `curso_disciplina`
--

INSERT INTO `curso_disciplina` (`id`, `id_curso`, `id_disciplina`, `semestre`) VALUES
(1, 1, 1, 1),
(2, 1, 2, 2),
(3, 1, 3, 3),
(4, 2, 4, 1),
(5, 2, 5, 2),
(6, 3, 1, 1),
(7, 3, 2, 2),
(8, 3, 3, 3);

-- --------------------------------------------------------

--
-- Estrutura para tabela `disciplina`
--

CREATE TABLE `disciplina` (
  `id_disciplina` int NOT NULL,
  `nome_disciplina` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Despejando dados para a tabela `disciplina`
--

INSERT INTO `disciplina` (`id_disciplina`, `nome_disciplina`) VALUES
(1, 'Algoritmos'),
(2, 'Banco de Dados'),
(3, 'Engenharia de Software'),
(4, 'Logística Básica'),
(5, 'Transporte e Distribuição');

-- --------------------------------------------------------

--
-- Estrutura para tabela `tbaluno`
--

CREATE TABLE `tbaluno` (
  `RA` int UNSIGNED NOT NULL,
  `Nome` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `celular` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data_nascimento` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cpf` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `endereco` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numero` int UNSIGNED DEFAULT NULL,
  `cep` char(9) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cidade` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uf` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_curso` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Despejando dados para a tabela `tbaluno`
--

INSERT INTO `tbaluno` (`RA`, `Nome`, `celular`, `data_nascimento`, `cpf`, `email`, `endereco`, `numero`, `cep`, `cidade`, `estado`, `uf`, `id_curso`) VALUES
(1001, 'João Silva', '11988887777', '2001-05-10', '12345678901', 'joao@email.com', 'Rua A', 10, '07000-000', 'Guarulhos', 'São Paulo', 'SP', 1),
(1002, 'Maria Santos', '11999996666', '2002-09-21', '23456789012', 'maria@email.com', 'Rua B', 20, '07000-000', 'Guarulhos', 'São Paulo', 'SP', 1),
(1003, 'Carlos Oliveira', '11977775555', '2003-01-15', '34567890123', 'carlos@email.com', 'Rua C', 30, '01100-000', 'São Paulo', 'São Paulo', 'SP', 3),
(1004, 'Ana Pereira', '11991112222', '2001-08-12', '45678901234', 'ana@email.com', 'Rua D', 40, '07000-100', 'Guarulhos', 'São Paulo', 'SP', 1),
(1005, 'Bruno Costa', '11992223333', '2002-03-25', '56789012345', 'bruno@email.com', 'Rua E', 50, '07000-200', 'Guarulhos', 'São Paulo', 'SP', 1),
(1006, 'Juliana Alves', '11993334444', '2000-11-10', '67890123456', 'juliana@email.com', 'Rua F', 60, '07000-300', 'Guarulhos', 'São Paulo', 'SP', 1),
(1007, 'Lucas Rocha', '11994445555', '2001-02-18', '78901234567', 'lucas@email.com', 'Rua G', 15, '07000-400', 'Guarulhos', 'São Paulo', 'SP', 2),
(1008, 'Fernanda Lima', '11995556666', '2003-07-28', '89012345678', 'fernanda@email.com', 'Rua H', 22, '07000-500', 'Guarulhos', 'São Paulo', 'SP', 2),
(1009, 'Ricardo Martins', '11996667777', '2002-10-05', '90123456789', 'ricardo@email.com', 'Rua I', 33, '07000-600', 'Guarulhos', 'São Paulo', 'SP', 2),
(1010, 'Carla Dias', '11997778888', '2001-01-01', '91234567890', 'carla@email.com', 'Rua J', 12, '01100-001', 'São Paulo', 'São Paulo', 'SP', 3),
(1011, 'Thiago Gomes', '11998889999', '2000-05-15', '92345678901', 'thiago@email.com', 'Rua K', 25, '01100-002', 'São Paulo', 'São Paulo', 'SP', 3),
(1012, 'Patrícia Souza', '11990001111', '2002-09-09', '93456789012', 'patricia@email.com', 'Rua L', 34, '01100-003', 'São Paulo', 'São Paulo', 'SP', 3),
(1013, 'Eduardo Santos', '11992345678', '2004-02-14', '76543219876', 'edu.santos@email.com', 'Rua M', 71, '07000-700', 'Guarulhos', 'São Paulo', 'SP', 1),
(1014, 'Marta Ribeiro', '11993456789', '2003-06-11', '87654321987', 'marta.ribeiro@email.com', 'Rua N', 82, '07000-800', 'Guarulhos', 'São Paulo', 'SP', 2),
(1015, 'Pedro Henrique', '11994567890', '2001-12-19', '98765432198', 'pedro.h@email.com', 'Rua O', 93, '01100-004', 'São Paulo', 'São Paulo', 'SP', 3),
(1016, 'Beatriz Mendes', '11995678901', '2003-03-03', '65432198765', 'bia.m@email.com', 'Av. Sul', 55, '07010-010', 'Guarulhos', 'São Paulo', 'SP', 1),
(1017, 'Gustavo Ferreira', '11996789012', '2002-04-04', '54321987654', 'gus.f@email.com', 'Av. Norte', 77, '07020-020', 'Guarulhos', 'São Paulo', 'SP', 2),
(1018, 'Larissa Cunha', '11997890123', '2001-05-05', '43219876543', 'la.cunha@email.com', 'Av. Leste', 88, '01110-100', 'São Paulo', 'São Paulo', 'SP', 3);

-- --------------------------------------------------------

--
-- Estrutura stand-in para view `v_consulta_alunos`
-- (Veja abaixo para a visão atual)
--
CREATE TABLE `v_consulta_alunos` (
`celular_formatado` varchar(19)
,`cidade` varchar(100)
,`cpf_formatado` varchar(14)
,`data_nascimento_formatada` varchar(10)
,`email` varchar(100)
,`endereco` varchar(255)
,`estado` varchar(50)
,`nome` varchar(50)
,`RA` int unsigned
,`uf` char(2)
);

-- --------------------------------------------------------

--
-- Estrutura para view `v_consulta_alunos`
--
DROP TABLE IF EXISTS `v_consulta_alunos`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_consulta_alunos`  AS SELECT `tbaluno`.`RA` AS `RA`, `tbaluno`.`Nome` AS `nome`, `tbaluno`.`email` AS `email`, `tbaluno`.`endereco` AS `endereco`, `tbaluno`.`cidade` AS `cidade`, `tbaluno`.`estado` AS `estado`, `tbaluno`.`uf` AS `uf`, (case when ((`tbaluno`.`data_nascimento` is not null) and (`tbaluno`.`data_nascimento` <> '')) then date_format(str_to_date(`tbaluno`.`data_nascimento`,'%Y-%m-%d'),'%d/%m/%Y') else NULL end) AS `data_nascimento_formatada`, (case when (length(`tbaluno`.`cpf`) = 11) then concat(substr(`tbaluno`.`cpf`,1,3),'.',substr(`tbaluno`.`cpf`,4,3),'.',substr(`tbaluno`.`cpf`,7,3),'-',substr(`tbaluno`.`cpf`,10,2)) else `tbaluno`.`cpf` end) AS `cpf_formatado`, (case when (length(`tbaluno`.`celular`) = 13) then concat('+',substr(`tbaluno`.`celular`,1,2),' (',substr(`tbaluno`.`celular`,3,2),') ',substr(`tbaluno`.`celular`,5,5),'-',substr(`tbaluno`.`celular`,10,4)) when (length(`tbaluno`.`celular`) = 11) then concat('(',substr(`tbaluno`.`celular`,1,2),') ',substr(`tbaluno`.`celular`,3,5),'-',substr(`tbaluno`.`celular`,8,4)) when (length(`tbaluno`.`celular`) = 10) then concat('(',substr(`tbaluno`.`celular`,1,2),') ',substr(`tbaluno`.`celular`,3,4),'-',substr(`tbaluno`.`celular`,7,4)) else `tbaluno`.`celular` end) AS `celular_formatado` FROM `tbaluno` ;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `aluno_disciplina`
--
ALTER TABLE `aluno_disciplina`
  ADD PRIMARY KEY (`id`),
  ADD KEY `aluno_ra` (`aluno_ra`),
  ADD KEY `id_curso_disciplina` (`id_curso_disciplina`);

--
-- Índices de tabela `aluno_nota_falta`
--
ALTER TABLE `aluno_nota_falta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_aluno_disciplina` (`id_aluno_disciplina`);

--
-- Índices de tabela `campus`
--
ALTER TABLE `campus`
  ADD PRIMARY KEY (`id_campus`);

--
-- Índices de tabela `curso`
--
ALTER TABLE `curso`
  ADD PRIMARY KEY (`id_curso`),
  ADD KEY `id_campus` (`id_campus`);

--
-- Índices de tabela `curso_disciplina`
--
ALTER TABLE `curso_disciplina`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_curso` (`id_curso`),
  ADD KEY `id_disciplina` (`id_disciplina`);

--
-- Índices de tabela `disciplina`
--
ALTER TABLE `disciplina`
  ADD PRIMARY KEY (`id_disciplina`);

--
-- Índices de tabela `tbaluno`
--
ALTER TABLE `tbaluno`
  ADD PRIMARY KEY (`RA`),
  ADD KEY `fk_aluno_curso` (`id_curso`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `aluno_disciplina`
--
ALTER TABLE `aluno_disciplina`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT de tabela `aluno_nota_falta`
--
ALTER TABLE `aluno_nota_falta`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT de tabela `curso_disciplina`
--
ALTER TABLE `curso_disciplina`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `aluno_disciplina`
--
ALTER TABLE `aluno_disciplina`
  ADD CONSTRAINT `aluno_disciplina_ibfk_1` FOREIGN KEY (`aluno_ra`) REFERENCES `tbaluno` (`RA`),
  ADD CONSTRAINT `aluno_disciplina_ibfk_2` FOREIGN KEY (`id_curso_disciplina`) REFERENCES `curso_disciplina` (`id`);

--
-- Restrições para tabelas `aluno_nota_falta`
--
ALTER TABLE `aluno_nota_falta`
  ADD CONSTRAINT `aluno_nota_falta_ibfk_1` FOREIGN KEY (`id_aluno_disciplina`) REFERENCES `aluno_disciplina` (`id`);

--
-- Restrições para tabelas `curso`
--
ALTER TABLE `curso`
  ADD CONSTRAINT `curso_ibfk_1` FOREIGN KEY (`id_campus`) REFERENCES `campus` (`id_campus`);

--
-- Restrições para tabelas `curso_disciplina`
--
ALTER TABLE `curso_disciplina`
  ADD CONSTRAINT `curso_disciplina_ibfk_1` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`),
  ADD CONSTRAINT `curso_disciplina_ibfk_2` FOREIGN KEY (`id_disciplina`) REFERENCES `disciplina` (`id_disciplina`);

--
-- Restrições para tabelas `tbaluno`
--
ALTER TABLE `tbaluno`
  ADD CONSTRAINT `fk_aluno_curso` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

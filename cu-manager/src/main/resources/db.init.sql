
INSERT ignore INTO `Role` (`id`, `description`) VALUES
 (1, 'ROLE_ADMIN'),
 (2, 'ROLE_USER');

INSERT ignore INTO `Image` (`id`, `name`, `path`, `version`, `cmd`, `status`, `imageType`, `managerName`) VALUES
 (1, 'mysql-5-5', 'cloudunit/mysql-5-5', '0.1', '', 1, 'module', 'phpmyadmin'),
 (2, 'tomcat-6', 'cloudunit/tomcat-appconf6', '0.1', '', 1, 'server',''),
 (3, 'tomcat-7', 'cloudunit/tomcat-appconf7', '0.1', '', 1, 'server',''),
 (4, 'tomcat-8', 'cloudunit/tomcat-appconf8', '0.1', '', 1, 'server',''),
 (5, 'jboss-7', 'cloudunit/jboss-appconf7', '0.1', '', 1, 'server',''),
 (6, 'jboss-8', 'cloudunit/jboss-appconf8', '0.1', '', 1, 'server',''),
 (7, 'jboss-5-1-0', 'cloudunit/jboss-appconf5-1-0', '0.1', '', 1, 'server',''),
 (8, 'git', 'cloudunit/git', '0.1', '', 1, 'tool',''),
 (9, 'mysql-5-5-data-db', 'cloudunit/mysql-5-5-data-db', '0.1', '', 1, 'tool',''),
 (10, 'postgresql-9-3-data-db', 'cloudunit/postgresql-9-3-data-db', '0.1', '', 1, 'tool',''),
 (11, 'postgresql-9-3', 'cloudunit/postgresql-9-3', '0.1', '', 1, 'module', 'phppgadmin'),
 (12, 'redis-lastest', 'cloudunit/redis-lastest', '0.1', '', 0, 'module', 'phpredmin'),
 (13, 'mongo-2-6', 'cloudunit/mongo-2-6', '0.1', '', 1, 'module','mms'),
 (14, 'mongo-2-6-data-db', 'cloudunit/mongo-2-6-data-db', '0.1', '', 1, 'tool','');

INSERT ignore INTO `User` (`id`,`firstName`, `lastName`, `email`, `password`, `role_id`,`status`,`signin`,`login`, `organization`) VALUES
 (1, 'John','Doe','johndoe.doe@gmail.com','cVwsWoHVZ28Qf9fHE0W4Qg==',1, 1,'2013-08-22 09:22:06','johndoe', 'admin');

INSERT ignore INTO `User` (`id`,`firstName`, `lastName`, `email`, `password`, `role_id`,`status`,`signin`,`login`, `organization`) VALUES
 (2, 'scott','tiger','scott.tiger@gmail.com','cVwsWoHVZ28Qf9fHE0W4Qg==',1, 1,'2014-02-22 09:22:06','scott', 'user');

INSERT IGNORE INTO `ProxySshPort` (`id`, `portNumber`, `used`) VALUES
 (1, '2000', 0),
 (2, '2001', 0),
 (3, '2002', 0),
 (4, '2003', 0),
 (5, '2004', 0),
 (6, '2005', 0),
 (7, '2006', 0),
 (8, '2007', 0),
 (10, '2009', 0),
 (11, '2010', 0),
 (12, '2011', 0),
 (13, '2012', 0),
 (14, '2013', 0),
 (15, '2014', 0),
 (16, '2015', 0),
 (17, '2016', 0),
 (18, '2017', 0),
 (19, '2018', 0),
 (20, '2019', 0),
 (21, '2020', 0),
 (22, '2021', 0),
 (23, '2022', 0),
 (24, '2023', 0),
 (25, '2024', 0),
 (26, '2025', 0),
 (27, '2026', 0),
 (28, '2027', 0),
 (29, '2028', 0),
 (30, '2029', 0),
 (31, '2030', 0),
 (32, '2031', 0),
 (33, '2032', 0),
 (34, '2033', 0),
 (35, '2034', 0),
 (36, '2035', 0),
 (37, '2036', 0),
 (38, '2037', 0),
 (39, '2038', 0),
 (40, '2039', 0),
 (41, '2040', 0),
 (42, '2041', 0),
 (43, '2042', 0),
 (44, '2043', 0),
 (45, '2044', 0),
 (46, '2045', 0),
 (47, '2046', 0),
 (48, '2047', 0),
 (49, '2048', 0),
 (50, '2049', 0),
 (51, '2050', 0),
 (52, '2051', 0),
 (53, '2052', 0),
 (54, '2053', 0),
 (55, '2054', 0),
 (56, '2055', 0),
 (57, '2056', 0),
 (58, '2057', 0),
 (59, '2058', 0),
 (60, '2059', 0),
 (61, '2060', 0),
 (62, '2061', 0),
 (63, '2062', 0),
 (64, '2063', 0),
 (65, '2064', 0),
 (66, '2065', 0),
 (67, '2066', 0),
 (68, '2067', 0),
 (69, '2068', 0),
 (70, '2069', 0),
 (71, '2070', 0),
 (72, '2071', 0),
 (73, '2072', 0),
 (74, '2073', 0),
 (75, '2074', 0),
 (76, '2075', 0),
 (77, '2076', 0),
 (78, '2077', 0),
 (79, '2078', 0),
 (80, '2079', 0),
 (81, '2080', 0),
 (82, '2081', 0),
 (83, '2082', 0),
 (84, '2083', 0),
 (85, '2084', 0),
 (86, '2085', 0),
 (87, '2086', 0),
 (88, '2087', 0),
 (89, '2088', 0),
 (90, '2089', 0),
 (91, '2090', 0),
 (92, '2091', 0),
 (93, '2092', 0),
 (94, '2093', 0),
 (95, '2094', 0),
 (96, '2095', 0),
 (97, '2096', 0),
 (98, '2097', 0),
 (99, '2098', 0),
 (100, '2099', 0),
 (101, '2100', 0),
 (102, '2101', 0),
 (103, '2102', 0),
 (104, '2103', 0),
 (105, '2104', 0),
 (106, '2105', 0),
 (107, '2106', 0),
 (108, '2107', 0),
 (109, '2108', 0),
 (110, '2109', 0),
 (111, '2110', 0),
 (112, '2111', 0),
 (113, '2112', 0),
 (114, '2113', 0),
 (115, '2114', 0),
 (116, '2115', 0),
 (117, '2116', 0),
 (118, '2117', 0),
 (119, '2118', 0),
 (120, '2119', 0),
 (121, '2120', 0),
 (122, '2121', 0),
 (123, '2122', 0),
 (124, '2123', 0),
 (125, '2124', 0),
 (126, '2125', 0),
 (127, '2126', 0),
 (128, '2127', 0),
 (129, '2128', 0),
 (130, '2129', 0),
 (131, '2130', 0),
 (132, '2131', 0),
 (133, '2132', 0),
 (134, '2133', 0),
 (135, '2134', 0),
 (136, '2135', 0),
 (137, '2136', 0),
 (138, '2137', 0),
 (139, '2138', 0),
 (140, '2139', 0),
 (141, '2140', 0),
 (142, '2141', 0),
 (143, '2142', 0),
 (144, '2143', 0),
 (145, '2144', 0),
 (146, '2145', 0),
 (147, '2146', 0),
 (148, '2147', 0),
 (149, '2148', 0),
 (150, '2149', 0),
 (151, '2150', 0),
 (152, '2151', 0),
 (153, '2152', 0),
 (154, '2153', 0),
 (155, '2154', 0),
 (156, '2155', 0),
 (157, '2156', 0),
 (158, '2157', 0),
 (159, '2158', 0),
 (160, '2159', 0),
 (161, '2160', 0),
 (162, '2161', 0),
 (163, '2162', 0),
 (164, '2163', 0),
 (165, '2164', 0),
 (166, '2165', 0),
 (167, '2166', 0),
 (168, '2167', 0),
 (169, '2168', 0),
 (170, '2169', 0),
 (171, '2170', 0),
 (172, '2171', 0),
 (173, '2172', 0),
 (174, '2173', 0),
 (175, '2174', 0),
 (176, '2175', 0),
 (177, '2176', 0),
 (178, '2177', 0),
 (179, '2178', 0),
 (180, '2179', 0),
 (181, '2180', 0),
 (182, '2181', 0),
 (183, '2182', 0),
 (184, '2183', 0),
 (185, '2184', 0),
 (186, '2185', 0),
 (187, '2186', 0),
 (188, '2187', 0),
 (189, '2188', 0),
 (190, '2189', 0),
 (191, '2190', 0),
 (192, '2191', 0),
 (193, '2192', 0),
 (194, '2193', 0),
 (195, '2194', 0),
 (196, '2195', 0),
 (197, '2196', 0),
 (198, '2197', 0),
 (199, '2198', 0),
 (200, '2199', 0),
 (201, '2200', 0),
 (202, '2201', 0),
 (203, '2202', 0),
 (204, '2203', 0),
 (205, '2204', 0),
 (206, '2205', 0),
 (207, '2206', 0),
 (208, '2207', 0),
 (209, '2208', 0),
 (210, '2209', 0),
 (211, '2210', 0),
 (212, '2211', 0),
 (213, '2212', 0),
 (214, '2213', 0),
 (215, '2214', 0),
 (216, '2215', 0),
 (217, '2216', 0),
 (218, '2217', 0),
 (219, '2218', 0),
 (220, '2219', 0),
 (221, '2220', 0),
 (222, '2221', 0),
 (223, '2222', 0),
 (224, '2223', 0),
 (225, '2224', 0),
 (226, '2225', 0),
 (227, '2226', 0),
 (228, '2227', 0),
 (229, '2228', 0),
 (230, '2229', 0),
 (231, '2230', 0),
 (232, '2231', 0),
 (233, '2232', 0),
 (234, '2233', 0),
 (235, '2234', 0),
 (236, '2235', 0),
 (237, '2236', 0),
 (238, '2237', 0),
 (239, '2238', 0),
 (240, '2239', 0),
 (241, '2240', 0),
 (242, '2241', 0),
 (243, '2242', 0),
 (244, '2243', 0),
 (245, '2244', 0),
 (246, '2245', 0),
 (247, '2246', 0),
 (248, '2247', 0),
 (249, '2248', 0),
 (250, '2249', 0),
 (251, '2250', 0),
 (252, '2251', 0),
 (253, '2252', 0),
 (254, '2253', 0),
 (255, '2254', 0),
 (256, '2255', 0),
 (257, '2256', 0),
 (258, '2257', 0),
 (259, '2258', 0),
 (260, '2259', 0),
 (261, '2260', 0),
 (262, '2261', 0),
 (263, '2262', 0),
 (264, '2263', 0),
 (265, '2264', 0),
 (266, '2265', 0),
 (267, '2266', 0),
 (268, '2267', 0),
 (269, '2268', 0),
 (270, '2269', 0),
 (271, '2270', 0),
 (272, '2271', 0),
 (273, '2272', 0),
 (274, '2273', 0),
 (275, '2274', 0),
 (276, '2275', 0),
 (277, '2276', 0),
 (278, '2277', 0),
 (279, '2278', 0),
 (280, '2279', 0),
 (281, '2280', 0),
 (282, '2281', 0),
 (283, '2282', 0),
 (284, '2283', 0),
 (285, '2284', 0),
 (286, '2285', 0),
 (287, '2286', 0),
 (288, '2287', 0),
 (289, '2288', 0),
 (290, '2289', 0),
 (291, '2290', 0),
 (292, '2291', 0),
 (293, '2292', 0),
 (294, '2293', 0),
 (295, '2294', 0),
 (296, '2295', 0),
 (297, '2296', 0),
 (298, '2297', 0),
 (299, '2298', 0),
 (300, '2299', 0),
 (301, '2300', 0),
 (302, '2301', 0),
 (303, '2302', 0),
 (304, '2303', 0),
 (305, '2304', 0),
 (306, '2305', 0),
 (307, '2306', 0),
 (308, '2307', 0),
 (309, '2308', 0),
 (310, '2309', 0),
 (311, '2310', 0),
 (312, '2311', 0),
 (313, '2312', 0),
 (314, '2313', 0),
 (315, '2314', 0),
 (316, '2315', 0),
 (317, '2316', 0),
 (318, '2317', 0),
 (319, '2318', 0),
 (320, '2319', 0),
 (321, '2320', 0),
 (322, '2321', 0),
 (323, '2322', 0),
 (324, '2323', 0),
 (325, '2324', 0),
 (326, '2325', 0),
 (327, '2326', 0),
 (328, '2327', 0),
 (329, '2328', 0),
 (330, '2329', 0),
 (331, '2330', 0),
 (332, '2331', 0),
 (333, '2332', 0),
 (334, '2333', 0),
 (335, '2334', 0),
 (336, '2335', 0),
 (337, '2336', 0),
 (338, '2337', 0),
 (339, '2338', 0),
 (340, '2339', 0),
 (341, '2340', 0),
 (342, '2341', 0),
 (343, '2342', 0),
 (344, '2343', 0),
 (345, '2344', 0),
 (346, '2345', 0),
 (347, '2346', 0),
 (348, '2347', 0),
 (349, '2348', 0),
 (350, '2349', 0),
 (351, '2350', 0),
 (352, '2351', 0),
 (353, '2352', 0),
 (354, '2353', 0),
 (355, '2354', 0),
 (356, '2355', 0),
 (357, '2356', 0),
 (358, '2357', 0),
 (359, '2358', 0),
 (360, '2359', 0),
 (361, '2360', 0),
 (362, '2361', 0),
 (363, '2362', 0),
 (364, '2363', 0),
 (365, '2364', 0),
 (366, '2365', 0),
 (367, '2366', 0),
 (368, '2367', 0),
 (369, '2368', 0),
 (370, '2369', 0),
 (371, '2370', 0),
 (372, '2371', 0),
 (373, '2372', 0),
 (374, '2373', 0),
 (375, '2374', 0),
 (376, '2375', 0),
 (377, '2376', 0),
 (378, '2377', 0),
 (379, '2378', 0),
 (380, '2379', 0),
 (381, '2380', 0),
 (382, '2381', 0),
 (383, '2382', 0),
 (384, '2383', 0),
 (385, '2384', 0),
 (386, '2385', 0),
 (387, '2386', 0),
 (388, '2387', 0),
 (389, '2388', 0),
 (390, '2389', 0),
 (391, '2390', 0),
 (392, '2391', 0),
 (393, '2392', 0),
 (394, '2393', 0),
 (395, '2394', 0),
 (396, '2395', 0),
 (397, '2396', 0),
 (398, '2397', 0),
 (399, '2398', 0),
 (400, '2399', 0),
 (401, '2400', 0),
 (402, '2401', 0),
 (403, '2402', 0),
 (404, '2403', 0),
 (405, '2404', 0),
 (406, '2405', 0),
 (407, '2406', 0),
 (408, '2407', 0),
 (409, '2408', 0),
 (410, '2409', 0),
 (411, '2410', 0),
 (412, '2411', 0),
 (413, '2412', 0),
 (414, '2413', 0),
 (415, '2414', 0),
 (416, '2415', 0),
 (417, '2416', 0),
 (418, '2417', 0),
 (419, '2418', 0),
 (420, '2419', 0),
 (421, '2420', 0),
 (422, '2421', 0),
 (423, '2422', 0),
 (424, '2423', 0),
 (425, '2424', 0),
 (426, '2425', 0),
 (427, '2426', 0),
 (428, '2427', 0),
 (429, '2428', 0),
 (430, '2429', 0),
 (431, '2430', 0),
 (432, '2431', 0),
 (433, '2432', 0),
 (434, '2433', 0),
 (435, '2434', 0),
 (436, '2435', 0),
 (437, '2436', 0),
 (438, '2437', 0),
 (439, '2438', 0),
 (440, '2439', 0),
 (441, '2440', 0),
 (442, '2441', 0),
 (443, '2442', 0),
 (444, '2443', 0),
 (445, '2444', 0),
 (446, '2445', 0),
 (447, '2446', 0),
 (448, '2447', 0),
 (449, '2448', 0),
 (450, '2449', 0),
 (451, '2450', 0),
 (452, '2451', 0),
 (453, '2452', 0),
 (454, '2453', 0),
 (455, '2454', 0),
 (456, '2455', 0),
 (457, '2456', 0),
 (458, '2457', 0),
 (459, '2458', 0),
 (460, '2459', 0),
 (461, '2460', 0),
 (462, '2461', 0),
 (463, '2462', 0),
 (464, '2463', 0),
 (465, '2464', 0),
 (466, '2465', 0),
 (467, '2466', 0),
 (468, '2467', 0),
 (469, '2468', 0),
 (470, '2469', 0),
 (471, '2470', 0),
 (472, '2471', 0),
 (473, '2472', 0),
 (474, '2473', 0),
 (475, '2474', 0),
 (476, '2475', 0),
 (477, '2476', 0),
 (478, '2477', 0),
 (479, '2478', 0),
 (480, '2479', 0),
 (481, '2480', 0),
 (482, '2481', 0),
 (483, '2482', 0),
 (484, '2483', 0),
 (485, '2484', 0),
 (486, '2485', 0),
 (487, '2486', 0),
 (488, '2487', 0),
 (489, '2488', 0),
 (490, '2489', 0),
 (491, '2490', 0),
 (492, '2491', 0),
 (493, '2492', 0),
 (494, '2493', 0),
 (495, '2494', 0),
 (496, '2495', 0),
 (497, '2496', 0),
 (498, '2497', 0),
 (499, '2498', 0),
 (500, '2499', 0);
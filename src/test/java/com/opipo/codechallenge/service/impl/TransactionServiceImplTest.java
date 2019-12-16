package com.opipo.codechallenge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import com.opipo.codechallenge.exception.AlreadyExistsException;
import com.opipo.codechallenge.exception.AmountIncorrectException;
import com.opipo.codechallenge.repository.TransactionRepository;
import com.opipo.codechallenge.repository.model.SortType;
import com.opipo.codechallenge.repository.model.TransactionEntity;
import com.opipo.codechallenge.service.TransactionAmountService;
import com.opipo.web.api.model.Transaction;

@ExtendWith(MockitoExtension.class)
@DisplayName("TansactionServiceImpl test")
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionAmountService transactionAmountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Comparator<TransactionEntity> transactionEntityAmountComparator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Given transaction ant the sumarized of previous transactions are GT 0, then save it")
    void givenTransactionAndThenSaveIt() {
        Transaction transaction = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity = Mockito.mock(TransactionEntity.class);
        BigDecimal transactionAmount = Mockito.mock(BigDecimal.class);
        BigDecimal transactionAmountSumarized = Mockito.mock(BigDecimal.class);
        Collection<TransactionEntity> transactions = Arrays.asList(new TransactionEntity[] {});
        String accountIban = "4815162342";

        Mockito.when(modelMapper.map(transaction, TransactionEntity.class)).thenReturn(transactionEntity);
        Mockito.when(transactionAmountService.calculateAmount(transactionEntity)).thenReturn(transactionAmount);
        Mockito.when(transactionEntity.getAccountIban()).thenReturn(accountIban);
        Mockito.when(transactionRepository.findAllByAccountIban(accountIban)).thenReturn(transactions);
        Mockito.when(transactionAmountService.sumarize(transactions)).thenReturn(transactionAmountSumarized);
        Mockito.when(transactionAmountService.checkSumIsGteZero(transactionAmount, transactionAmountSumarized))
                .thenReturn(true);

        transactionService.save(transaction);

        Mockito.verify(transactionRepository).save(transactionEntity);
    }

    @Test
    @DisplayName("Given transaction ant the sumarized of previous transactions are LT 0, then throws exception")
    void givenTransactionAndThenSaveItThrowsException() {
        Transaction transaction = new Transaction();
        String reference = "myReference89";
        String accountIban = "4815162342-2";
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccountIban(accountIban);
        transaction.setReference(reference);
        BigDecimal transactionAmount = BigDecimal.valueOf(341d);
        BigDecimal transactionAmountSumarized = BigDecimal.valueOf(352d);
        Collection<TransactionEntity> transactions = Arrays.asList(new TransactionEntity[] {});

        Mockito.when(modelMapper.map(transaction, TransactionEntity.class)).thenReturn(transactionEntity);
        Mockito.when(transactionAmountService.calculateAmount(transactionEntity)).thenReturn(transactionAmount);
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.empty());
        Mockito.when(transactionRepository.findAllByAccountIban(accountIban)).thenReturn(transactions);
        Mockito.when(transactionAmountService.sumarize(transactions)).thenReturn(transactionAmountSumarized);
        Mockito.when(transactionAmountService.checkSumIsGteZero(transactionAmount, transactionAmountSumarized))
                .thenReturn(false);
        assertThrows(AmountIncorrectException.class, () -> transactionService.save(transaction));

        Mockito.verify(transactionRepository, Mockito.never()).save(transactionEntity);
    }

    @Test
    @DisplayName("Given accountIban and no sort then return list with all elements")
    void givenAccountIbanAndNoSortThenGetList() {
        String accountIban = "myIban";
        SortType sortType = null;
        Transaction transaction1 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity1 = Mockito.mock(TransactionEntity.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity2 = Mockito.mock(TransactionEntity.class);

        List<TransactionEntity> transactionEntities = Arrays.asList(transactionEntity1, transactionEntity2);

        Mockito.when(transactionRepository.findAllByAccountIban(accountIban)).thenReturn(transactionEntities);

        Mockito.when(modelMapper.map(transactionEntity1, Transaction.class)).thenReturn(transaction1);
        Mockito.when(modelMapper.map(transactionEntity2, Transaction.class)).thenReturn(transaction2);

        List<Transaction> actual = transactionService.list(accountIban, sortType);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.contains(transaction1));
        assertTrue(actual.contains(transaction2));
    }

    @Test
    @DisplayName("Given no accountIban and no sort then return list with all elements")
    void givenNoAccountIbanAndNoSortThenGetList() {
        String accountIban = null;
        SortType sortType = null;
        Transaction transaction1 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity1 = Mockito.mock(TransactionEntity.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity2 = Mockito.mock(TransactionEntity.class);

        List<TransactionEntity> transactionEntities = Arrays.asList(transactionEntity1, transactionEntity2);

        Mockito.when(transactionRepository.findAll()).thenReturn(transactionEntities);

        Mockito.when(modelMapper.map(transactionEntity1, Transaction.class)).thenReturn(transaction1);
        Mockito.when(modelMapper.map(transactionEntity2, Transaction.class)).thenReturn(transaction2);

        List<Transaction> actual = transactionService.list(accountIban, sortType);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.contains(transaction1));
        assertTrue(actual.contains(transaction2));
    }

    @Test
    @DisplayName("Given accountIban and sortAsc then return sorted list")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void givenAccountIbanAndAscSortThenGetList() {
        givenAccountIbanSortedThenGetList(true);
    }

    @Test
    @DisplayName("Given accountIban and sortDesc then return sorted list")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void givenAccountIbanAndDescSortThenGetList() {
        givenAccountIbanSortedThenGetList(false);
    }

    @Test
    @DisplayName("Given reference that exists then get it exists")
    void givenReferenceThenExists() {
        String reference = "myReferenceExists4815162342";
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.of(new TransactionEntity()));
        assertTrue(transactionService.referenceExists(reference));
    }

    @Test
    @DisplayName("Given reference that not exists then get it not exists")
    void givenReferenceThenNoExists() {
        String reference = "myReference";
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.empty());
        assertFalse(transactionService.referenceExists(reference));
    }

    @Test
    @DisplayName("Given no reference then get it not exists")
    void givenNoReferenceThenNoExists() {
        String reference = null;
        assertFalse(transactionService.referenceExists(reference));
    }

    @Test
    @DisplayName("Given transaction and the reference exists then throw exception")
    void givenTransactionWithDuplicatedReferenceThrowException() {
        String reference = "myId23";
        Transaction transaction = new Transaction();
        transaction.setReference(reference);
        Mockito.when(transactionRepository.findById(reference)).thenReturn(Optional.of(new TransactionEntity()));
        assertThrows(AlreadyExistsException.class, () -> transactionService.save(transaction));
    }

    private static Stream<Arguments> provideInvalidIban() {
        return Stream.of(Arguments.of("ES66210004184012345678910000000000"), Arguments.of("6000491500051234567892"),
                Arguments.of("ES-9420805801101234567891"), Arguments.of("9000246912501234567891ES"),
                Arguments.of("ES71 0030 2053 09 1234567895"), Arguments.of("ES10 0049 2352 0824 1420 5416"),
                Arguments.of(""));
    }

    private static Stream<Arguments> provideValidIban() {
        return Stream.of(Arguments.of("ES6621000418401234567891"), Arguments.of("ES6000491500051234567892"),
                Arguments.of("ES9420805801101234567891"), Arguments.of("ES9000246912501234567891"),
                Arguments.of("ES7100302053091234567895"), Arguments.of("ES1000492352082414205416"),
                Arguments.of("ES1720852066623456789011"), Arguments.of("AD3204726066756121381806"),
                Arguments.of("AE140378736251783237400"), Arguments.of("AL05288026784726516421150117"),
                Arguments.of("AT685231453184848297"), Arguments.of("AZ47EYTH08868606254286715493"),
                Arguments.of("BA460935266539489024"), Arguments.of("BE02640235339006"),
                Arguments.of("BG09DUJQ01912889051307"), Arguments.of("BH54VVOR61871059649541"),
                Arguments.of("BR5425723570269926230634776U1"), Arguments.of("BY06042388960958071603076318"),
                Arguments.of("CH9169615220271665636"), Arguments.of("CR57646061002876288465"),
                Arguments.of("CY21747455922272295596070505"), Arguments.of("CZ8871334491301587481726"),
                Arguments.of("DE40931223770621497964"), Arguments.of("DK0371841682063299"),
                Arguments.of("DO52359690070619961539651083"), Arguments.of("EE659813581070748119"),
                Arguments.of("ES9504293424325425680864"), Arguments.of("FI8296921896593658"),
                Arguments.of("FO3885813274257988"), Arguments.of("FR4091209423956909727801809"),
                Arguments.of("GB69RAJK59832224777899"), Arguments.of("GE22TI6590447555532957"),
                Arguments.of("GI16HXKE903673279732680"), Arguments.of("GL4270781940237989"),
                Arguments.of("GR7706156436932989308631740"), Arguments.of("GT69101720052346792467417946"),
                Arguments.of("HR5298551067211490666"), Arguments.of("HU43847164102361127869975481"),
                Arguments.of("IE76CHHI10864468112775"), Arguments.of("IL219717441904993442908"),
                Arguments.of("IQ55CNKW712448669375697"), Arguments.of("IS079716156609366769108245"),
                Arguments.of("IT51D6063581056113793963546"), Arguments.of("JO55MWOX4684747489684292557379"),
                Arguments.of("KW46CPWJ1852228985369152254905"), Arguments.of("KZ461733600808653416"),
                Arguments.of("LB40315201510241332192755328"), Arguments.of("LI4116595848654221847"),
                Arguments.of("LT065283507348247261"), Arguments.of("LU486486249311559628"),
                Arguments.of("LV19IOXY3748308310181"), Arguments.of("MC3873661683284858920171571"),
                Arguments.of("MD1009583116964097309521"), Arguments.of("ME02466789269167523666"),
                Arguments.of("MK89335799945877948"), Arguments.of("MR8437150247056049688309489"),
                Arguments.of("MT89KNYU70653654469216112429936"), Arguments.of("MU76AYSX9343398503933510622LJK"),
                Arguments.of("NL66MCTM5404988134"), Arguments.of("NO2334926152433"),
                Arguments.of("PK91DSEN8336735174747819"), Arguments.of("PL44385751275096604402294436"),
                Arguments.of("PS24BXQE073858647976996532806"), Arguments.of("PT28617280935565630798962"),
                Arguments.of("QA89YLUY199115199705679725834"), Arguments.of("RO39AJNT1862919616148371"),
                Arguments.of("RS16156815459321977161"), Arguments.of("SA3898145588516177863338"),
                Arguments.of("SC84OTNV30626624828474215364WJD"), Arguments.of("SE4792363541605900508731"),
                Arguments.of("SI67552049050735196"), Arguments.of("SK8113032915126337200583"),
                Arguments.of("SM52P5821017632866339271561"), Arguments.of("ST59840624344393678699111"),
                Arguments.of("SV52CKMA77564930209936720375"), Arguments.of("TL640096007061168608568"),
                Arguments.of("TN7972586018947529831378"), Arguments.of("TR538044242978294127345370"),
                Arguments.of("UA721455599032878743688601031"), Arguments.of("VA70944869683743371584"),
                Arguments.of("VG98AXZM6454296825292119"), Arguments.of("XK775462455238322666"));
    }

    @ParameterizedTest(name = "Invalid iban {0}")
    @MethodSource("provideInvalidIban")
    @EmptySource
    @DisplayName("Given accountIban invalid then check is invalid")
    @Execution(ExecutionMode.CONCURRENT)
    void givenBadIbanThenCheckIsInvalid(String iban) {
        assertFalse(transactionService.validateIban(iban));
    }

    @ParameterizedTest(name = "Valid iban {0}")
    @MethodSource("provideValidIban")
    @NullSource
    @DisplayName("Given accountIban valid then check is valid")
    @Execution(ExecutionMode.CONCURRENT)
    void givenIbanThenCheckIsValid(String iban) {
        assertTrue(transactionService.validateIban(iban));
    }

    void givenAccountIbanSortedThenGetList(Boolean asc) {
        String accountIban = "myIban";
        SortType sortType = asc ? SortType.ASC : SortType.DESC;
        Transaction transaction1 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity1 = Mockito.mock(TransactionEntity.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity2 = Mockito.mock(TransactionEntity.class);
        Transaction transaction3 = Mockito.mock(Transaction.class);
        TransactionEntity transactionEntity3 = Mockito.mock(TransactionEntity.class);

        List<TransactionEntity> transactionEntities = Arrays.asList(transactionEntity1, transactionEntity2,
                transactionEntity3);

        Mockito.when(transactionRepository.findAllByAccountIban(accountIban)).thenReturn(transactionEntities);

        Mockito.when(modelMapper.map(transactionEntity1, Transaction.class)).thenReturn(transaction1);
        Mockito.when(modelMapper.map(transactionEntity2, Transaction.class)).thenReturn(transaction2);
        Mockito.when(modelMapper.map(transactionEntity3, Transaction.class)).thenReturn(transaction3);

        mockO1IsGtO2(transactionEntity2, transactionEntity1);
        mockO1IsGtO2(transactionEntity2, transactionEntity3);
        mockO1IsGtO2(transactionEntity3, transactionEntity1);

        List<Transaction> actual = transactionService.list(accountIban, sortType);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertCorrectOrder(actual, transaction1, transaction2, transaction3, asc);
    }

    private void assertCorrectOrder(List<Transaction> actual, Transaction transaction1, Transaction transaction2,
            Transaction transaction3, Boolean asc) {
        if (asc) {
            assertEquals(transaction1, actual.get(0));
            assertEquals(transaction3, actual.get(1));
            assertEquals(transaction2, actual.get(2));
        } else {
            assertEquals(transaction2, actual.get(0));
            assertEquals(transaction3, actual.get(1));
            assertEquals(transaction1, actual.get(2));
        }
    }

    private void mockO1IsGtO2(TransactionEntity o1, TransactionEntity o2) {
        Mockito.when(transactionEntityAmountComparator.compare(o1, o2)).thenReturn(1);
        Mockito.when(transactionEntityAmountComparator.compare(o2, o1)).thenReturn(-1);
    }

}
